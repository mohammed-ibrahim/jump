package org.jump.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class CacheManager {

    private static Map<String, List<String>> items = new HashMap<String, List<String>>();

    public static void addEntry(String cacheKey, List<String> ids) {
        if (!items.keySet().contains(cacheKey)) {
            items.put(cacheKey, new ArrayList<String>());
        }

        items.get(cacheKey).addAll(ids);
    }

    public static Iterator<String> getIteratorForKey(String key) {
        if (!items.containsKey(key)) {
            String message = "Cache key missing: " + key;
            throw new RuntimeException(message);
        }

        return items.get(key).iterator();
    }

    public static Set<String> allkeys() {
        return items.keySet();
    }

    public static String itemsForKey(String key) {
        return StringUtils.join(items.get(key), ",");
    }
}
