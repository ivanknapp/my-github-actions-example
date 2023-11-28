package ru.knapp.springdemotest.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LinkedHashMapLRUCache<K, V> implements Cache<K, V> {

    private final LinkedHashMap<K, V> cache;
    private final int maxCapacity;
    private final ReentrantReadWriteLock readWriteLock;

    public LinkedHashMapLRUCache(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.cache = new LinkedHashMap<>(maxCapacity, 0.75f, true) {

            /**
             *
             * @param eldest The least recently inserted entry in the map, or if
             *           this is an access-ordered map, the least recently accessed
             *           entry.  This is the entry that will be removed it this
             *           method returns {@code true}.  If the map was empty prior
             *           to the {@code put} or {@code putAll} invocation resulting
             *           in this invocation, this will be the entry that was just
             *           inserted; in other words, if the map contains a single
             *           entry, the eldest entry is also the newest.
             * @return
             */
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxCapacity;
            }
        };
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<V> get(K k) {
        try {
            //блокируется записью, но позволяет читать параллельно
            this.readWriteLock.readLock().lock();

            if (cache.containsKey(k)) {
                //так как access order = true элемент сам попадет в MRU
                return Optional.of(cache.get(k));
            }
        } finally {
            this.readWriteLock.readLock().unlock();
        }

        return Optional.empty();
    }

    @Override
    public void put(K k, V v) {
        try {
            //блокирует чтение, но блокируется если уже читают и ждет
            this.readWriteLock.writeLock().lock();
            System.out.printf("q size =%s, %s \n", cache.size(), Thread.currentThread().getName());
            if (cache.containsKey(k)) {
                //если уже есть удалим
                cache.remove(k);
            }
//            else if (cache.size() >= maxCapacity) {
//                //удаляем последний, так как он LRU
//                cache.remove(cache.keySet().iterator().next());
//            }
            //положили в MRU
            cache.put(k, v);
            System.out.printf("q size =%s, %s\n", cache.size(), Thread.currentThread().getName());
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        try {
            this.readWriteLock.readLock().lock();
            return cache.size();
        } finally {
            this.readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void print() {
        System.out.println("-----PRINT START-----");
        try {
            this.readWriteLock.readLock().lock();
            Iterator<K> iterator = cache.keySet().iterator();
            cache.forEach((k, v) -> {
                System.out.printf("k=[%s], v=[%s]%n", k, v);
            });

        } finally {
            this.readWriteLock.readLock().unlock();
        }
        System.out.println("-----PRINT END-----");
    }

    @Override
    public void clear() {
        try {
            this.readWriteLock.writeLock().lock();

            this.cache.clear();
        } finally {
            this.readWriteLock.writeLock().unlock();
        }
    }
}
