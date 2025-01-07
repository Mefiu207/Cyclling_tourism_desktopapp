package com.project;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EntityScan(basePackages = "com.project.springbootjavafx.models")
@EnableJpaRepositories(basePackages = "com.project.springbootjavafx.repositories")
@SpringBootApplication(scanBasePackages = {"com.project"})
public class SpringBootConfig {

}
