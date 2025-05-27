package com.example.springapi;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @GetMapping("/login")
    public Map<String, Object> login(@RequestParam String email, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();

        if (email.equals("user@example.com") && password.equals("1234")) {
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", "abc123");
        } else {
            response.put("success", false);
            response.put("message", "Invalid credentials");
        }

        return response;
    }
    @GetMapping("/")
    public String home() {
        return "Welcome to the login server!";
    }

}
