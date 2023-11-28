package ru.knapp.springdemotest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.knapp.springdemotest.client.MyIntegrationClient;
import ru.knapp.springdemotest.service.SomeService;
import ru.knapp.springdemotest.service.UnControllableService;


@RestController
@RequestMapping(path = Controller.MAIN_URL)
@RequiredArgsConstructor
public class Controller {
    protected static final String MAIN_URL = "/main";
    protected static final String UNCONTROLLABLE_SERVICE = "/uncontrollable";
    protected static final String REMOTE_SERVICE = "/remote-service";
    protected static final String PARAM_NAME = "name";
    protected static final String PARAM_URL = "url";

    private final SomeService someService;
    private final UnControllableService unControllableService;
    private final MyIntegrationClient myIntegrationClient;


    @GetMapping
    public ResponseEntity<String> getControlledValue(@RequestParam(value = PARAM_NAME) String name) {
        return ResponseEntity.ok(someService.doSomething(name));
    }

    @GetMapping(path = UNCONTROLLABLE_SERVICE)
    public ResponseEntity<String> getUnControlledValue() {
        return ResponseEntity.ok(unControllableService.getSomeUnControllableValue());
    }

    @GetMapping(value = REMOTE_SERVICE, params = {PARAM_URL})
    public String executeRemote(@RequestParam(value = PARAM_URL) String url) {
        return myIntegrationClient.executeGet(url);
    }

    @GetMapping(value = REMOTE_SERVICE, params = {PARAM_NAME})
    public String executeRemoteWithName(@RequestParam(PARAM_NAME) String name) {
        return myIntegrationClient.executeMyGet() + " " + name;
    }
}
