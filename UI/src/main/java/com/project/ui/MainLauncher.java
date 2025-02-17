package com.project.ui;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * The {@code MainLauncher} class serves as the entry point for the application.
 * It is annotated with Spring Boot configuration annotations to enable component scanning,
 * entity scanning, and repository support.
 *
 * <p>The annotations used are:</p>
 * <ul>
 *   <li>{@code @EntityScan} - Scans the specified packages for JPA entities.</li>
 *   <li>{@code @EnableJpaRepositories} - Enables Spring Data JPA repositories in the specified packages.</li>
 *   <li>{@code @SpringBootApplication} - Marks this class as a Spring Boot application and enables component scanning.</li>
 * </ul>
 *
 * <p>The main method launches the JavaFX application by delegating to the {@link AppLayout} class.</p>
 */
@EntityScan(basePackages = "com.project.springbootjavafx.models")
@EnableJpaRepositories(basePackages = "com.project.springbootjavafx.repositories")
@SpringBootApplication(scanBasePackages = {"com.project"})
public class MainLauncher {

    /**
     * The entry point of the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Application.launch(AppLayout.class, args);
    }
}
