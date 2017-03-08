package org.jump.datagen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.FieldConfig;
import org.jump.util.Utility;

public class DatabaseRowFetcher implements IField {

    private List<String> items = null;

    private Iterator<String> iterator = null;

    private Integer factor;

    private Integer factorCount;

    private String lastItem;

    public DatabaseRowFetcher(ApplicationConfiguration appConfig, FieldConfig fieldConfig) {


        if (fieldConfig.getParams().size() < 1 || fieldConfig.getParams().size() > 2) {
            throw new RuntimeException("from_sql usage: from_sql('sql') or from_sql('sql', 'factor')");
        }

        if (fieldConfig.getParams().size() == 2 && !Utility.isNumeric(fieldConfig.getParams().get(1))) {
            throw new RuntimeException("Parameter 2 passed to from_sql('sql', 'factor') should be integer");
        }

        try {
            this.items = this.getItemsFromDb(appConfig, fieldConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.factorCount = 0;
        this.factor = fieldConfig.getParams().size() == 2 ? Integer.parseInt(fieldConfig.getParams().get(1)) : 1;
    }

    @Override
    public String getNext() {
        if (this.factorCount < this.factor) {
            this.factorCount = this.factorCount + 1;

            if (this.lastItem == null) {
                this.lastItem = fetchNext();
            }

            return this.lastItem;
        } else {

            this.factorCount = 0;
            this.lastItem = fetchNext();

            return this.lastItem;
        }
    }

    private String fetchNext() {
        if (this.iterator == null|| !this.iterator.hasNext()) {
            this.iterator = this.items.iterator();
        }

        return this.iterator.next();
    }

    private List<String> getItemsFromDb(ApplicationConfiguration appConfig, FieldConfig fieldConfig) throws Exception {

        String url = Utility.buildUrl(appConfig);
        Connection connection = DriverManager.getConnection(url, appConfig.getUser(), appConfig.getPassword());
        String sql = fieldConfig.getParams().get(0);

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);

        List<String> items = new ArrayList<String>();
        while (resultSet.next()) {
            items.add(resultSet.getString(0));
        }

        resultSet.close();
        stmt.close();
        connection.close();

        if (items.size() == 0) {
            String message = String.format("The Sql: %s did not return any rows", sql);
            throw new RuntimeException(message);
        }

        return items;
    }

}
