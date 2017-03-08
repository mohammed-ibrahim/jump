package org.jump.datagen;

import org.jump.factory.FakeDataFactory;
import org.jump.parser.FieldConfig;

public class FakeFieldWrapper implements IField {

    private FieldConfig fieldConfig;

    public FakeFieldWrapper(FieldConfig fieldConfig) {

        if (fieldConfig.getParams().size() != 1) {
            throw new RuntimeException("Method fake takes only 1 parameter.");
        }

        this.fieldConfig = fieldConfig;
    }

    @Override
    public String getNext() {
        return FakeDataFactory.getData(fieldConfig.getParams().get(0));
    }

}
