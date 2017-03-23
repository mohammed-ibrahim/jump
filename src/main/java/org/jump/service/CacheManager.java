package org.jump.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class CacheManager {

    private static CacheManager instance = new CacheManager();

    public static CacheManager getInstance() {
        return instance;
    }

    private CacheManager() {

    }

    private Map<String, List<String>> items = new HashMap<String, List<String>>();

    private Map<String, String> variables = new HashMap<String, String>();

    public void addInsertedIdSet(String cacheKey, List<String> ids) {
        if (!this.items.keySet().contains(cacheKey)) {
            this.items.put(cacheKey, new ArrayList<String>());
        }

        this.items.get(cacheKey).addAll(ids);
    }

    public Iterator<String> getIteratorForInsertedIds(String key) {
        if (!this.items.containsKey(key)) {
            String message = "Cache key missing: " + key;
            throw new RuntimeException(message);
        }

        return this.items.get(key).iterator();
    }

    public Set<String> allInsertedIdKeys() {
        return this.items.keySet();
    }

    public String itemsForInsertedIdKey(String key) {
        return StringUtils.join(this.items.get(key), ",");
    }

    public void addVariable(String variableName, String variableValue) {
        this.variables.put(variableName, variableValue);
    }

    public String getVariable(String variableName) {
        if(!this.variables.containsKey(variableName)) {
            String message = String.format("Variable [%s] not defined.", variableName);
            throw new RuntimeException(message);
        }

        return this.variables.get(variableName);
    }
}
