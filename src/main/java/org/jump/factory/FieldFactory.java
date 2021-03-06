package org.jump.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jump.datagen.BetweenFieldGenerator;
import org.jump.datagen.CachedKeysFetcher;
import org.jump.datagen.DatabaseRowFetcher;
import org.jump.datagen.FakeFieldWrapper;
import org.jump.datagen.IField;
import org.jump.datagen.NowField;
import org.jump.datagen.RandomListPicker;
import org.jump.datagen.RandomRangeGenerator;
import org.jump.datagen.SerialListItemPicker;
import org.jump.datagen.StaticField;
import org.jump.entity.ApplicationConfiguration;
import org.jump.parser.FieldConfig;
import org.jump.parser.InsertCommand;
import org.jump.service.TransactionalSqlExecutor;

public class FieldFactory {

    private enum Method {
        ONE_OF,
        STATIC,
        SERIAL,
        FROM_SQL,
        BETWEEN,
        RANDOM_BETWEEN,
        FAKE,
        NOW,
        INSERTED_IDS;
    }

    public List<IField> build(ApplicationConfiguration appConfig, InsertCommand insertCommand, TransactionalSqlExecutor sqlExecutor) {
        List<IField> fields = new ArrayList<IField>();

        for (FieldConfig fieldConfig: insertCommand.getFieldConfigs()) {
            fields.add(buildField(appConfig, fieldConfig, sqlExecutor));
        }

        return fields;
    }

    private IField buildField(ApplicationConfiguration appConfig, FieldConfig fieldConfig, TransactionalSqlExecutor sqlExecutor) {
        String fnName = fieldConfig.getFnName();
        Method method = null;

        try {
            method = Method.valueOf(fnName.toUpperCase());
        } catch (Exception e) {

            List<String> methodNames = new ArrayList<String>();
            for (Method methodType: Method.values()) {
                methodNames.add(methodType.toString());
            }

            @SuppressWarnings("unchecked")
            String message = String.format("Unknown method: [%s], Only supported values are: %s", fnName.toUpperCase(), StringUtils.join(methodNames));

            throw new RuntimeException(message);
        }

        switch (method) {
            case ONE_OF:
                return new RandomListPicker(fieldConfig);

            case STATIC:
                return new StaticField(fieldConfig);

            case SERIAL:
                return new SerialListItemPicker(fieldConfig);

            case FROM_SQL:
                return new DatabaseRowFetcher(appConfig, fieldConfig, sqlExecutor);

            case BETWEEN:
                return new BetweenFieldGenerator(fieldConfig);

            case RANDOM_BETWEEN:
                return new RandomRangeGenerator(fieldConfig);

            case FAKE:
                return new FakeFieldWrapper(fieldConfig);

            case NOW:
                return new NowField();

            case INSERTED_IDS:
                return new CachedKeysFetcher(fieldConfig);

            default:
                throw new RuntimeException(String.format("Function [%s] not implemented yet, please contact support.", method.toString().toUpperCase()));
        }
    }
}
