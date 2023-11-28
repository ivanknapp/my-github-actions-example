package ru.knapp.springdemotest.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Random;

@Component
public class InsertRandomAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Field declaredField : bean.getClass().getDeclaredFields()) {
            Optional.ofNullable(declaredField.getAnnotation(InsertRandom.class))
                .ifPresent(a -> {
                    int min = a.min();
                    int max = a.max();
                    int rand = min + new Random().nextInt(max - min);
                    declaredField.setAccessible(true);
                    System.out.println("Set [%s]=[%s] to [%s]".formatted(declaredField.getName(), rand, bean.getClass()));
                    ReflectionUtils.setField(declaredField, bean, rand);
                });
        }


        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }
}
