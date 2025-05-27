package com.example.springapi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    public class LoginController {

        @GetMapping("/hello")
        public String hello() {
            return "Hello from your Spring Boot server!";
        }
    }

