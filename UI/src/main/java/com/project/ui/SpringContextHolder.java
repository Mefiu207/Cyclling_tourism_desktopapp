package com.project.ui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


// Klasa trzymająca context pozwalająca na prosty dostęp do wszyskich Beanów
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
