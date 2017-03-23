package org.jump.parser;

import java.util.*;

import lombok.Getter;
import lombok.Setter;

public class FieldConfig {

    @Getter
    @Setter
    private String fieldName;

    @Getter
    @Setter
    private String fnName;

    @Getter
    @Setter
    private ArrayList<String> params;

    public FieldConfig(String fieldName, String fnName, ArrayList<String> params) {
        this.fieldName = fieldName;
        this.fnName = fnName;
        this.params = params;
    }

    public FieldConfig() {
        params = new ArrayList<String>();
    }

    public String toString() {
        return fieldName + " " + fnName + " " + params.toString();
    }
}
