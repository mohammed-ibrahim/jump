package org.jump.factory;

import java.util.ArrayList;
import java.util.List;

import org.jump.datagen.DataRowGenerator;
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

    public List<DataRowGenerator> build(InsertCommand insertCommand) {
        List<DataRowGenerator> fields = new ArrayList<DataRowGenerator>();

        for (FieldConfig fieldConfig: insertCommand.getFieldConfigs()) {
            fields.add(buildField(fieldConfig));
        }
        // TODO Auto-generated method stub
        return null;
    }

    private DataRowGenerator buildField(FieldConfig fieldConfig) {
        String fnName = fieldConfig.getFnName();
        Method method = null;

        try {
            method = Method.valueOf(fnName);
        } catch (Exception e) {

            throw new RuntimeException("Unknown function: " + fnName);
        }

        switch (method) {
        case BETWEEN:
            break;
        }
        return null;
    }

}
