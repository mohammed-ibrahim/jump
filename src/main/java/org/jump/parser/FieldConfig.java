package org.jump.parser;

import java.util.*;


public class FieldConfig {
    private String fieldName;

    private String fnName;

    private ArrayList<String> params;

    public FieldConfig(String fieldName, String fnName, ArrayList<String> params) {
        this.fieldName = fieldName;
        this.fnName = fnName;
        this.params = params;
    }

    public FieldConfig() {
        params = new ArrayList<String>();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFnName() {
        return fnName;
    }

    public void setFnName(String fnName) {
        this.fnName = fnName;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public String toString() {
        return fieldName + " " + fnName + " " + params.toString();
    }
}
