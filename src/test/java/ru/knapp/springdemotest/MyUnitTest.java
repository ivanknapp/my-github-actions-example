package ru.knapp.springdemotest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.knapp.springdemotest.service.SomeService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.knapp.springdemotest.service.SomeService.RESPONSE_PATTERN;

class MyUnitTest {

    private final SomeService someService = new SomeService();

    @ParameterizedTest
    @CsvSource({
        "Ivan",
        "Oleg",
        "Andrew",
    })
    public void shouldReturnSomething(String name) {
        String actual = someService.doSomething(name);
        String expected = RESPONSE_PATTERN.formatted(name);
        assertEquals(expected, actual);
    }

}
