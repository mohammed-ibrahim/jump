package org.jump.datagen;

import org.jump.parser.FieldConfig;

public class StaticField implements IField {

    private String data;

    public StaticField(FieldConfig fieldConfig) {
        if (fieldConfig.getParams().size() != 1) {
            throw new RuntimeException("Method static takes only 1 parameter.");
        }

        this.data = fieldConfig.getParams().get(0);
    }

    @Override
    public String getNext() {
        return this.data;
    }

}
