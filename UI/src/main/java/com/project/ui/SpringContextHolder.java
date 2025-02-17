package com.project.ui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * The {@code SpringContextHolder} class holds a static reference to the Spring
 * {@link ApplicationContext}, providing simple and global access to Spring-managed beans.
 *
 * <p>
 * By implementing {@link ApplicationContextAware}, this class automatically receives
 * the {@link ApplicationContext} during initialization and stores it in a static variable.
 * This enables retrieval of the context via the getter from lombok from anywhere
 * in the application.
 * </p>
 *
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Component}- Marks this class as a Spring component, ensuring it is
 *   detected during component scanning and managed by the Spring container.</li>
 * </ul>
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    @Getter
    private static ApplicationContext context;

    /**
     * Sets the Spring {@link ApplicationContext} that will be held statically.
     *
     * @param applicationContext the Spring application context to set
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.context = applicationContext;
    }

}
