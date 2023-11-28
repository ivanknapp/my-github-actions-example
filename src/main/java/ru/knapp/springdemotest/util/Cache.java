package ru.knapp.springdemotest.util;

import java.util.Optional;

public interface Cache<K, V> {

    Optional<V> get(K k);

    void put(K k, V v);

    int size();

    void print();

    void clear();
}
