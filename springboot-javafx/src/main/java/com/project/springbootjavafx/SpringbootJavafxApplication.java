package com.project.springbootjavafx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@RestController
public class SpringbootJavafxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJavafxApplication.class, args);
    }

    @GetMapping
    public String hello(){ return "Elo"; }

}
