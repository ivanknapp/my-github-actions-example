package ru.knapp.springdemotest.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ProfilingAnnotationBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Class> profilingBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        boolean isAnnotated = Optional.ofNullable(bean.getClass().getAnnotation(Profiling.class)).isPresent();
        if (isAnnotated) {
            profilingBeans.put(beanName, bean.getClass());
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = profilingBeans.get(beanName);

        if (beanClass == null) {
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }

        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(),
            (proxy, method, args) -> {

                long before = System.currentTimeMillis();
                Object retVal = method.invoke(bean, args);
                long after = System.currentTimeMillis();
                System.out.println("Invoke [%s] with args[%s] by [%s] sec".formatted(method.getName(), args, after - before));

                return retVal;
            });
    }
}
