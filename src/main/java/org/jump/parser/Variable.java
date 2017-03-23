package org.jump.parser;

import org.jump.service.CacheManager;
import org.jump.util.Utility;

import lombok.Getter;
import lombok.Setter;

public class Variable {

    @Getter
    @Setter
    private VariableType type;

    @Getter
    @Setter
    private String value;

    public Variable(VariableType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Integer getIntValue() {
        switch (this.type) {
        case NAMED:
            return getDoubleValue(CacheManager.getInstance().getVariable(this.value)).intValue();

        case STATIC:
            return getDoubleValue(this.value).intValue();

        default:
            String message = "Variable type not configured: " + this.type.toString();
            throw new RuntimeException(message);
        }
    }

    private Double getDoubleValue(String item) {
        if (!Utility.isNumeric(item)) {
            String message = String.format("Variable %s assignemnt should be an integer", item);
            throw new RuntimeException(message);
        }

        return Double.parseDouble(item);
    }
}
