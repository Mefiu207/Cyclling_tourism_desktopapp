package com.project.ui;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;



@EntityScan(basePackages = "com.project.springbootjavafx.models")
@EnableJpaRepositories(basePackages = "com.project.springbootjavafx.repositories")
@SpringBootApplication(scanBasePackages = {"com.project"})
public class MainLauncher {

    public static void main(String[] args) {
        Application.launch(AppLayout.class, args);
    }
}
