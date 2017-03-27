package org.jump.datagen;

import java.util.Iterator;

import org.jump.parser.FieldConfig;
import org.jump.service.CacheManager;
import org.jump.util.Utility;

public class CachedKeysFetcher implements IField {

    private FieldConfig fieldConfig;

    private Iterator<String> iterator;

    private String cacheKey;

    private String lastItem;

    private int factor;

    private int factorCount;

    public CachedKeysFetcher(FieldConfig fieldConfiguration) {
        int paramSize = fieldConfiguration.getParams().size();
        boolean parametersMismatch = paramSize < 1 || paramSize > 2;
        String message = "Invalid usage of method: inserted_ids\n";
        message += "Usage: inserted_ids('key') or inserted_ids('key', 'factor')";
        Utility.throwIfTrue(parametersMismatch, message);

        String param2 = fieldConfiguration.getParams().size() == 2 ? fieldConfiguration.getParams().get(1) : null;

        if (param2 != null) {
            Utility.throwIfTrue(
                !Utility.isNumeric(param2),
                "Parameter 2 passed to inserted_ids('sql', 'factor') should be integer");

            int value = Utility.getDoubleValue(param2).intValue();
            Utility.throwIfTrue(value < 1, "'factor' for method inserted_ids(key, factor) has to be atleast 1");
            this.factor = value;
        } else {
            this.factor = 1;
        }

        this.iterator = null;
        this.fieldConfig = fieldConfiguration;
        this.cacheKey = this.fieldConfig.getParams().get(0);
        this.lastItem = null;
        this.factorCount = 0;
    }

    @Override
    public String getNext() {
        if (this.factorCount < this.factor) {
            this.factorCount = this.factorCount + 1;

            if (this.lastItem == null) {

                this.lastItem = fetchNext();
            }

            return Utility.wrap(this.lastItem);
        } else {

            this.factorCount = 1;
            this.lastItem = fetchNext();

            return Utility.wrap(this.lastItem);
        }
    }

    private String fetchNext() {
        if (this.iterator == null || !this.iterator.hasNext()) {
            this.iterator = CacheManager.getInstance().getIteratorForInsertedIds(this.cacheKey);
        }

        return this.iterator.next();
    }
}
