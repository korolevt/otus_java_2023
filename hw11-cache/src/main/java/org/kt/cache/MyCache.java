package org.kt.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;


public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K,V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);

        logger.info("put key:{}, value: {}", key, value);
        notify(key, value, "put element");
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);

        logger.info("remove key:{}, value: {}", key, value);
        notify(key, value, "remove element");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);

        logger.info("get key:{}, value: {}", key, value);
        notify(key, value, "get element");

        return value;
    }

    private void notify(K key, V value, String action) {
        for (var listener : listeners) {
            try {
                listener.notify(key, value, action);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

}