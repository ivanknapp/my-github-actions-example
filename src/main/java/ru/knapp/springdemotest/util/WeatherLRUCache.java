package ru.knapp.springdemotest.util;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WeatherLRUCache<K, V> implements Cache<K, V> {

    private final int maxCacheSize;
    private final ReentrantReadWriteLock lock;
    private final Map<K, V> map;
    private final Queue<K> queue;

    public WeatherLRUCache(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        this.map = new HashMap<>(maxCacheSize);
        this.queue = new LinkedList<>();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public Optional<V> get(K id) {
        lock.readLock().lock();
        try {
            Optional<V> weatherInCityOpt = Optional.ofNullable(map.get(id));
            if (weatherInCityOpt.isPresent()) {
                V weatherInCity = weatherInCityOpt.get();
                queue.remove(weatherInCity);
                queue.add(id);
                return weatherInCityOpt;
            }
            return weatherInCityOpt;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void put(K k, V weatherInCity) {
        try {
            lock.writeLock().lock();
            System.out.printf("q size =%s, map size=%s %s \n", queue.size(), map.size(), Thread.currentThread().getName());
            if (queue.size() == maxCacheSize) {
                K weatherInCityId = queue.poll();
                map.remove(weatherInCityId);
            }

            map.put(k, weatherInCity);
            queue.add(k);
            System.out.printf("q size =%s, map size=%s %s\n", queue.size(), map.size(), Thread.currentThread().getName());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.readLock().lock();
            return map.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void print() {
        try {
            lock.readLock().lock();

            System.out.println("-----PRINT START-----");
            for (K k : queue) {
                System.out.printf("k=[%s], v=[%s]%n", k, map.get(k));
            }
            System.out.println("-----PRINT END-----");
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        try {
            this.lock.writeLock().lock();
            this.map.clear();
            this.queue.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

}
