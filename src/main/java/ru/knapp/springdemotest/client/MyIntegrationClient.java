package ru.knapp.springdemotest.client;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Data
@Component
public class MyIntegrationClient {
    private final RestTemplate restTemplate;
    @Value("${my.integration.url}")
    private String MyIntegrationUrl;

    public String executeMyGet() {
        return restTemplate.getForObject(MyIntegrationUrl, String.class);
    }

    public String executeGet(String url) {
        return restTemplate.getForObject(url, String.class);
    }

}
