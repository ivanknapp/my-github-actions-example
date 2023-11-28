package ru.knapp.springdemotest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import ru.knapp.springdemotest.util.Cache;
import ru.knapp.springdemotest.util.LinkedHashMapLRUCache;
import ru.knapp.springdemotest.util.WeatherLRUCache;

@SpringBootApplication
public class SpringDemoTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoTestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public Cache<Integer, String> myCache() {
//        return new LinkedHashMapLRUCache<Integer, String>(50);
//    }

    @Bean
    public Cache<Integer, String> myCache() {
        return new WeatherLRUCache<Integer, String>(50);
    }
}
