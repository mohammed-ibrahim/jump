package org.jump.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jump.entity.ApplicationConfiguration;
import org.jump.util.Utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionalSqlExecutor {

    private Connection connection;

    private ApplicationConfiguration appConfig;

    public TransactionalSqlExecutor(ApplicationConfiguration appConfig) throws Exception {

        String url = Utility.buildUrl(appConfig);
        this.connection = DriverManager.getConnection(url, appConfig.getUser(), appConfig.getPassword());
        this.connection.setAutoCommit(false);
        this.appConfig = appConfig;
    }

    public void executeUpdate(String sql) throws Exception {
        if (this.appConfig.isLogSql()) {
            System.out.println("EXECUTING: " + sql);
        }

        try {
            Statement stmt = this.connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR WITH SQL: " + sql);

            throw e;
        }
    }

    public List<String> executeUpdateWithImpactedIds(String sql) throws Exception {
        if (this.appConfig.isLogSql()) {
            System.out.println("EXECUTING: " + sql);
        }

        try {
            Statement stmt = this.connection.createStatement();
            int affectedRows = stmt.executeUpdate(sql);

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            ResultSet generatedKeys = stmt.getGeneratedKeys();

            List<String> impactedIds = new ArrayList<String>();

            while (generatedKeys.next()) {
                impactedIds.add(generatedKeys.getString(1));
            }

            stmt.close();

            return impactedIds;
        } catch (Exception e) {
            System.out.println("ERROR WITH SQL: " + sql);

            throw e;
        }
    }

    public List<String> getItemsFromSql(String sql) throws Exception {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);

        List<String> items = new ArrayList<String>();
        while (resultSet.next()) {
            items.add(resultSet.getString(1));
        }

        stmt.close();
        return items;
    }

    public void commitAndClose() throws Exception {
        if (this.connection != null) {
            this.connection.commit();
            this.connection.close();
        }
    }

    public void rollbackAndClose() throws Exception {
        if (this.connection != null) {
            this.connection.rollback();
            this.connection.close();
        }
    }
}
