package org.jump.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jump.datagen.IField;
import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.FieldConfig;
import org.jump.parser.InsertCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportHandler {

    private static int MAX_ROWS_PER_ITERATION = 1000;

    private static String OPEN_PAREN = "(";

    private static String CLOSE_PAREN = ")";

    private static String SEPERATOR = ",";

    private List<IField> fields;

    private InsertCommand insertCommand;

    public void importRows(ApplicationConfiguration appConfig, InsertCommand insertCommand, List<IField> fields) {
        this.fields = fields;
        this.insertCommand = insertCommand;

        StringBuilder sb = new StringBuilder();
        String prefix = "";

        for (int i=0; i<insertCommand.getNumRows(); i++) {

            sb.append(prefix);
            sb.append(OPEN_PAREN);
            sb.append(getRow());
            sb.append(CLOSE_PAREN);
            prefix = SEPERATOR;

            if (i % MAX_ROWS_PER_ITERATION == 0) {

                log.info(getInsertPrefix() + sb.toString());
            }
        }

        if (sb.length() > 0) {
            log.info(getInsertPrefix() + sb.toString());
        }
    }

    private String getInsertPrefix() {
        List<String> columnNames = new ArrayList<String>();

        for (FieldConfig fieldConfig: insertCommand.getFieldConfigs()) {
            columnNames.add(fieldConfig.getFieldName());
        }

        return String.format("INSERT INTO %s (%s)", this.insertCommand.getTableName(), StringUtils.join(columnNames, ","));
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
