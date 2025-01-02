package com.project.ui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EntityScan(basePackages = "com.project.springbootjavafx.models")
@EnableJpaRepositories(basePackages = "com.project.springbootjavafx.repositories")
@SpringBootApplication(scanBasePackages = {"com.project.springbootjavafx"})
public class SpringBootConfig {

}
