package ru.knapp.springdemotest.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import ru.knapp.springdemotest.client.MyIntegrationClient;
import wiremock.org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.knapp.springdemotest.controller.Controller.MAIN_URL;
import static ru.knapp.springdemotest.controller.Controller.REMOTE_SERVICE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
class MyIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    MyIntegrationClient myIntegrationClient;
    @Autowired
    RestTemplate restTemplate;
    @Value("classpath:__files/executeGet.txt")
    Resource resource;

    @ParameterizedTest
    @CsvSource({
        "ivan",
        "roman"
    })
    @SneakyThrows
    public void shouldReturnSomething(String name) {
        String actual = restTemplate.getForObject("http://localhost:%s%s%s?name=%s".formatted(port, MAIN_URL, REMOTE_SERVICE, name), String.class);

        String resourceVal = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        String expected = "%s %s".formatted(resourceVal, name);
        assertEquals(expected, actual);
    }

}
