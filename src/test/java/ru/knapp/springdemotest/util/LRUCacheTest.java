package ru.knapp.springdemotest.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LRUCacheTest {

    @Autowired
    private Cache<Integer, String> cache;

    @BeforeEach
    public void clear() {
        cache.clear();
    }

    public static void main(String[] args) {
        Queue<Object> objects = new LinkedList<>();

        for (int i = 0; i < 55; i++) {
            objects.add(i);
            System.out.println(i + " " + objects.size());
        }
    }

    @RepeatedTest(130)
    public void testAutowiredCache() throws Exception {
        final int size = 50;
//        Cache cache = new WeatherLRUCache(size);
        final ExecutorService executorService = Executors.newFixedThreadPool(15);
        CountDownLatch countDownLatch = new CountDownLatch(size);
        try {

            for (int i = 0; i < size; i++) {
                int finalI = i;
                Runnable runnable =() -> {
                    int key = finalI;
                    cache.put(key, "value" + key);
                    countDownLatch.countDown();
                };
                executorService.submit(runnable);
            }
//            IntStream.range(0, size).<Runnable>mapToObj(key -> () -> {
//                cache.put(key, "value" + key);
//                countDownLatch.countDown();
//            }).forEach(executorService::submit);
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }

        assertEquals(size, cache.size());

//        IntStream.range(0, size)
//            .forEach(i -> assertEquals("value" + i, cache.get(i).get()));

//        cache.print();
    }


    @RepeatedTest(30)
    public void runMultiThreadTask_WhenPutDataInConcurrentToCache_ThenNoDataLost() throws Exception {
        final int size = 50;
        final ExecutorService executorService = Executors.newFixedThreadPool(15);
        Cache<Integer, String> cache = new LinkedHashMapLRUCache<>(size);
        CountDownLatch countDownLatch = new CountDownLatch(size);
        try {
            IntStream.range(0, size).<Runnable>mapToObj(key -> () -> {
                cache.put(key, "value" + key);
                countDownLatch.countDown();
            }).forEach(executorService::submit);
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }

        assertEquals(size, cache.size());

        IntStream.range(0, size)
            .forEach(i -> assertEquals("value" + i, cache.get(i).get()));

        cache.print();
    }

    @Test
    public void happyCaseTest() throws Exception {
        final int size = 5;
        Cache<Integer, String> cache = new LinkedHashMapLRUCache<>(size);
        cache.put(1, "value" + 1);
        cache.put(2, "value" + 2);
        cache.put(3, "value" + 3);
        cache.put(4, "value" + 4);
        cache.put(5, "value" + 5);
        cache.print();

        cache.put(6, "value" + 6);//remove 1 and push 6 to MRU
        cache.print();

        cache.put(7, "value" + 7);//remove 2 and push 7 to MRU
        cache.print();

        assertTrue(cache.get(1).isEmpty());
        assertTrue(cache.get(2).isEmpty());

        cache.put(8, "value" + 8);//remove 3 and push 8 to MRU
        cache.print();

        assertTrue(cache.get(4).isPresent());//push 4 to MRU
        cache.print();

        cache.put(9, "value" + 9);//remove 5 and push 9 to MRU
        cache.print();

        assertTrue(cache.get(5).isEmpty());

        assertEquals(size, cache.size());
    }


}