package com.github.mekuanent.encryption.registry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Binds Easy access {@link ApplicationContext application context}
 * Source https://github.com/brunozambiazi/blog/tree/master/spring-application-context
 * @author brunozambiazi
 */
@Component
public class ContextHelper implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * Get a Spring bean by type.
     **/
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }

    /**
     * Get a Spring bean by name.
     **/
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
}
