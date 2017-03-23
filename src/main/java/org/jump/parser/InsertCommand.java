package org.jump.parser;

import java.util.*;
import org.jump.parser.grammer.*;

public class InsertCommand implements Command {
    private String tableName;

    private Integer numRows;

    private String storageIdentifier;

    private ArrayList<FieldConfig> fieldConfigs = new ArrayList<FieldConfig>();

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getNumRows() {
        return this.numRows;
    }

    public void setNumRows(Integer numRows) {
        this.numRows = numRows;
    }

    public ArrayList<FieldConfig> getFieldConfigs() {
        return this.fieldConfigs;
    }

    public void setFieldConfigs(ArrayList<FieldConfig> fieldConfigs) {
        this.fieldConfigs = fieldConfigs;
    }

    public String toString() {
        return this.tableName + " " + this.numRows.toString() + " " + this.fieldConfigs.toString();
    }

    public String getStorageIdentifier() {
        return storageIdentifier;
    }

    public void setStorageIdentifier(String storageIdentifier) {
        this.storageIdentifier = storageIdentifier;
    }
}
