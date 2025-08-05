package com.example.springapi.Controller;

import com.example.springapi.model.LoginRequest;
import com.example.springapi.model.LoginResponse;
import com.example.springapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import com.example.springapi.repository.UserRepository;


@RestController
public class LoginController {
    @Autowired
    private UserRepository userRepository;
     private String email;
     private String password;




    @PostMapping("/api/login")
    @Transactional(readOnly = true)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // ✅ LOG the incoming request
        System.out.println("Attempt login with email: " + request.getEmail());
        System.out.println("Password entered: " + request.getPassword());
        User user = userRepository.findByEmail(request.getEmail());
        // ✅ LOG user info if found
        if (user != null) {
            System.out.println("User found: " + user.getEmail());
            System.out.println("Password in DB: " + user.getPassword());
        }

        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Invalid credentials", null));
        }
        return ResponseEntity.ok(new LoginResponse(true, "Login successful", user));
       // return ResponseEntity.ok(new LoginResponse(true, "Login successful", null));
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to the login server!";
    }

}
