package org.jump.datagen;

import java.util.Iterator;

import org.jump.parser.FieldConfig;
import org.jump.service.CacheManager;
import org.jump.util.Utility;

public class CachedKeysFetcher implements IField {

    private FieldConfig fieldConfig;

    private Iterator<String> iterator;

    private String cacheKey;

    public CachedKeysFetcher(FieldConfig fieldConfiguration) {
        if (fieldConfiguration.getParams().size() < 1 || fieldConfiguration.getParams().size() > 2) {
            throw new RuntimeException("inserted_ids usage: inserted_ids('key') or inserted_ids('key', 'factor')");
        }

        if (fieldConfiguration.getParams().size() == 2 && !Utility.isNumeric(fieldConfiguration.getParams().get(1))) {
            throw new RuntimeException("Parameter 2 passed to inserted_ids('sql', 'factor') should be integer");
        }

        this.fieldConfig = fieldConfiguration;
        this.cacheKey = this.fieldConfig.getParams().get(0);
    }

    @Override
    public String getNext() {

        if (this.iterator == null || !this.iterator.hasNext()) {
            this.iterator = CacheManager.getInstance().getIteratorForInsertedIds(this.cacheKey);
        }

        return this.iterator.next();
    }

}
