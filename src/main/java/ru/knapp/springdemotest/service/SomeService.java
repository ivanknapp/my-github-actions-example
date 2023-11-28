package ru.knapp.springdemotest.service;

import org.springframework.stereotype.Service;

@Service
public class SomeService {

    public static final String RESPONSE_PATTERN = "Hello %s";


    public String doSomething(String name) {
        return RESPONSE_PATTERN.formatted(name);
    }
}
