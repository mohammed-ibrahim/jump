package org.jump.factory;

import java.util.ArrayList;
import java.util.List;

import org.jump.datagen.BetweenFieldGenerator;
import org.jump.datagen.IField;
import org.jump.parser.FieldConfig;
import org.jump.parser.InsertCommand;

public class FieldFactory {

    private enum Method {
        ONE_OF,
        STATIC,
        SERIAL,
        FROM_SQL,
        BETWEEN,
        RANDOM_BETWEEN,
        FAKE,
        NOW;
    }

    public List<IField> build(InsertCommand insertCommand) {
        List<IField> fields = new ArrayList<IField>();

        for (FieldConfig fieldConfig: insertCommand.getFieldConfigs()) {
            fields.add(buildField(fieldConfig));
        }

        return fields;
    }

    private IField buildField(FieldConfig fieldConfig) {
        String fnName = fieldConfig.getFnName();
        Method method = null;

        try {
            method = Method.valueOf(fnName.toUpperCase());
        } catch (Exception e) {

            throw new RuntimeException("Unknown function: " + fnName);
        }

        switch (method) {
        case BETWEEN:
            return new BetweenFieldGenerator(fieldConfig);

        default:
            throw new RuntimeException("Unknown function name: " + method.toString());
        }
    }
}
