package org.jump.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jump.datagen.IField;
import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.FieldConfig;
import org.jump.parser.InsertCommand;


public class ImportHandler {

    private static int MAX_ROWS_PER_ITERATION = 1000;

    private static String OPEN_PAREN = "(";

    private static String CLOSE_PAREN = ")";

    private static String SEPERATOR = ",";

    private static String ROW_SEPERATOR = ",\n";

    private List<IField> fields;

    private InsertCommand insertCommand;

    private TransactionalSqlExecutor sqlExecutor;

    public ImportHandler(TransactionalSqlExecutor sqlExecutor, ApplicationConfiguration appConfig) {
        this.sqlExecutor = sqlExecutor;
    }

    public void importRows(ApplicationConfiguration appConfig, InsertCommand insertCommand, List<IField> fields) throws Exception {
        this.fields = fields;
        this.insertCommand = insertCommand;

        StringBuilder sb = new StringBuilder();
        String prefix = "";

        for (int i=0; i<insertCommand.getNumRows(); i++) {

            sb.append(prefix);
            sb.append(OPEN_PAREN);
            sb.append(getRow());
            sb.append(CLOSE_PAREN);
            prefix = ROW_SEPERATOR;

            if ((i > 0) && (i % MAX_ROWS_PER_ITERATION == 0)) {

                String sql = getInsertPrefix() + sb.toString();
                this.sqlExecutor.executeUpdate(sql);
                sb.setLength(0);
                prefix = "";
            }
        }

        if (sb.length() > 0) {
            String sql = getInsertPrefix() + sb.toString();
            this.sqlExecutor.executeUpdate(sql);
            sb.setLength(0);
            prefix = "";
        }
    }

    private String getInsertPrefix() {
        List<String> columnNames = new ArrayList<String>();

        for (FieldConfig fieldConfig: insertCommand.getFieldConfigs()) {
            columnNames.add(fieldConfig.getFieldName());
        }

        return String.format("INSERT INTO %s (%s) VALUES ", this.insertCommand.getTableName(), StringUtils.join(columnNames, ","));
    }

    private String getRow() {
        StringBuilder sb = new StringBuilder();
        String prefix = "";

        for (IField field: this.fields) {
            sb.append(prefix);
            sb.append(field.getNext());
            prefix = SEPERATOR;
        }

        return sb.toString();
    }
}
