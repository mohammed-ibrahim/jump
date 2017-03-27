package org.jump.datagen;

import java.util.Iterator;
import java.util.List;

import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.FieldConfig;
import org.jump.service.TransactionalSqlExecutor;
import org.jump.util.Utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseRowFetcher implements IField {

    private List<String> items = null;

    private Iterator<String> iterator = null;

    private Integer factor;

    private Integer factorCount;

    private String lastItem;

    private TransactionalSqlExecutor sqlExecutor;

    public DatabaseRowFetcher(ApplicationConfiguration appConfig, FieldConfig fieldConfig, TransactionalSqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
        int paramSize = fieldConfig.getParams().size();
        boolean parametersMismatch = paramSize < 1 || paramSize > 2;
        String message = "Invalid usage of method: from_sql\n";
        message += "Usage: inserted_ids('sql') or inserted_ids('sql', 'factor')";
        Utility.throwIfTrue(parametersMismatch, message);

        String param2 = paramSize > 1 ? fieldConfig.getParams().get(1) : null;
        Utility.throwIfTrue(
            paramSize > 1 && !Utility.isNumeric(param2),
            "Parameter 2 passed to from_sql('sql', 'factor') should be integer"
        );

        try {
            this.items = this.getItemsFromDb(appConfig, fieldConfig);
        } catch (Exception e) {
            System.out.println("Error with sql: " + fieldConfig.getParams().get(0));
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

            return Utility.wrapAndEscape(this.lastItem);
        } else {

            this.factorCount = 1;
            this.lastItem = fetchNext();

            return Utility.wrapAndEscape(this.lastItem);
        }
    }

    private String fetchNext() {
        if (this.iterator == null || !this.iterator.hasNext()) {
            this.iterator = this.items.iterator();
        }

        return this.iterator.next();
    }

    private List<String> getItemsFromDb(ApplicationConfiguration appConfig, FieldConfig fieldConfig) throws Exception {
        String sql = fieldConfig.getParams().get(0);

        List<String> items = this.sqlExecutor.getItemsFromSql(sql);

        if (items.size() == 0) {
            String message = String.format("The Sql: %s did not return any rows", sql);
            throw new RuntimeException(message);
        }

        return items;
    }

}
