package ru.knapp.springdemotest.util;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

@Data
public class LinkedListAndHashMapLRUCache<K, V> implements Cache<K, V> {
    private final int maxCapacity;
    private final Map<K, V> cache;
    private final LinkedList<K> order;

    public LinkedListAndHashMapLRUCache(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.cache = new HashMap<>(maxCapacity);
        this.order = new LinkedList<>();
    }

    @Override
    public Optional<V> get(K k) {
        V res = cache.get(k);
        if (res != null) {
            //меняем порядок, удаляем текущий и добавляем в начало то есть делаем его MRU
            order.remove(k);
            order.addFirst(k);
        }

        return Optional.ofNullable(res);
    }

    @Override
    public void put(K k, V v) {
        // если объем уже максимальный
        if (cache.size() >= maxCapacity) {
            // удаляем LRU элемент
            K keyRemoved = order.removeLast();
            cache.remove(keyRemoved);
        }

        // добавляем элемент, делаем его MRU
        order.addFirst(k);
        cache.put(k, v);
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public void print() {
        System.out.println("-----PRINT START-----");
        for (K k : order) {
            System.out.printf("k=[%s], v=[%s]%n", k, cache.get(k));
        }
        System.out.println("-----PRINT END-----");
    }

    @Override
    public void clear() {
        try {

            this.cache.clear();
            this.order.clear();
        }finally {
        }
    }

}
