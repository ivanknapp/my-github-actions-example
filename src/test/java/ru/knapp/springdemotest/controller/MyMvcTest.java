package ru.knapp.springdemotest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.knapp.springdemotest.service.UnControllableService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.knapp.springdemotest.controller.Controller.*;
import static ru.knapp.springdemotest.service.SomeService.RESPONSE_PATTERN;

@SpringBootTest
@AutoConfigureMockMvc
class MyMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UnControllableService unControllableService;

    @ParameterizedTest
    @CsvSource({
        "Ivan,",
//        ",",
        "Andrew,",
    })
    public void shouldReturnSomething(String name) throws Exception {
        this.mockMvc.perform(get(MAIN_URL).param(PARAM_NAME, name))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo(RESPONSE_PATTERN.formatted(name))));
    }

    @Test
    public void shouldReturnSomething2() throws Exception {
        this.mockMvc.perform(get(MAIN_URL))
            .andDo(print())
            .andExpect(status().is4xxClientError())
        ;
    }

    @Test
    public void shouldReturnWhatIWant() throws Exception {
        String expected = "Math return 1";
        when(unControllableService.getSomeUnControllableValue()).thenReturn(expected);

        this.mockMvc.perform(get(MAIN_URL + UNCONTROLLABLE_SERVICE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo(expected)));
    }

}
