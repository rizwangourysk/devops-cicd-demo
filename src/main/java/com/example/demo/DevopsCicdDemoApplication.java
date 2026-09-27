package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DevopsCicdDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevopsCicdDemoApplication.class, args);
    }

    @GetMapping("/")
    public String home() {
        return "Hello from DevOps CI/CD!";
    }
}
