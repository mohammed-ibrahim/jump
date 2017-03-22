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

    public void addEntry(String cacheKey, List<String> ids) {
        if (!this.items.keySet().contains(cacheKey)) {
            this.items.put(cacheKey, new ArrayList<String>());
        }

        this.items.get(cacheKey).addAll(ids);
    }

    public Iterator<String> getIteratorForKey(String key) {
        if (!this.items.containsKey(key)) {
            String message = "Cache key missing: " + key;
            throw new RuntimeException(message);
        }

        return this.items.get(key).iterator();
    }

    public Set<String> allkeys() {
        return this.items.keySet();
    }

    public String itemsForKey(String key) {
        return StringUtils.join(this.items.get(key), ",");
    }
}
