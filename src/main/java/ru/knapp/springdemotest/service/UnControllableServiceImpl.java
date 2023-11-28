package ru.knapp.springdemotest.service;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.knapp.springdemotest.bpp.DeprecatedClass;
import ru.knapp.springdemotest.bpp.InsertRandom;
import ru.knapp.springdemotest.bpp.Profiling;

@Profiling
@Service
@DeprecatedClass(newImpl = UnControllableServiceImprove.class)
public class UnControllableServiceImpl implements UnControllableService {

    @InsertRandom(min = 0, max = 5)
    private int random;

    public UnControllableServiceImpl() {
        System.out.println("PHASE 1");
    }

    @PostConstruct
    public void init() {
        System.out.println("PHASE 2");
    }

    @SneakyThrows
    public String getSomeUnControllableValue() {
        Thread.sleep((long) (1000 * Math.random() * 5));
        return "Now random return %s".formatted(random);
    }
}
