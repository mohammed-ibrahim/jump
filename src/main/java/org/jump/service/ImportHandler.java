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

    public ImportHandler(TransactionalSqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    public void importRows(InsertCommand insertCommand, List<IField> fields) throws Exception {
        this.fields = fields;
        this.insertCommand = insertCommand;

        StringBuilder sb = new StringBuilder();
        String prefix = "";

        for (int i=0; i<insertCommand.getNumRows().getIntValue(); i++) {

            sb.append(prefix);
            sb.append(OPEN_PAREN);
            sb.append(getRow());
            sb.append(CLOSE_PAREN);
            prefix = ROW_SEPERATOR;

            if ((i > 0) && (i % MAX_ROWS_PER_ITERATION == 0)) {

                String sql = getInsertPrefix() + sb.toString();
                executeWithCacheEntry(sql);
                sb.setLength(0);
                prefix = "";
            }
        }

        if (sb.length() > 0) {
            String sql = getInsertPrefix() + sb.toString();
            executeWithCacheEntry(sql);
            sb.setLength(0);
            prefix = "";
        }
    }

    private void executeWithCacheEntry(String sql) throws Exception {
        if (this.insertCommand.getStorageIdentifier() != null
            && this.insertCommand.getStorageIdentifier().isEmpty()) {

            throw new RuntimeException("Cache key entry for insert(.., .., 'cache_key_entry') cannot be null");
        }

        if (this.insertCommand.getStorageIdentifier() != null
            && !this.insertCommand.getStorageIdentifier().isEmpty()) {

            List<String> insertedIds = this.sqlExecutor.executeUpdateWithImpactedIds(sql);
            CacheManager.getInstance().addInsertedIdSet(this.insertCommand.getStorageIdentifier(), insertedIds);
        } else {

            this.sqlExecutor.executeUpdate(sql);
        }
    }

    private String getInsertPrefix() {
        List<String> columnNames = new ArrayList<String>();

        for (FieldConfig fieldConfig: insertCommand.getFieldConfigs()) {
            columnNames.add(fieldConfig.getFieldName());
        }

        return String.format("INSERT INTO %s (%s) VALUES \n", this.insertCommand.getTableName(), StringUtils.join(columnNames, ","));
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
