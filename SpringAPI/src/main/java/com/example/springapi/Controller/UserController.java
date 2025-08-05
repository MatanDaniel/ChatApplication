package com.example.springapi.Controller;

import com.example.springapi.model.User;
import com.example.springapi.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // Optional: allow Android to access this endpoint
public class UserController {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    @Transactional(readOnly = true) // tells Spring to manage the session properly,
                                    // including disabling auto-commit while streaming
                                    // BLOBs like images.
    public List<User> getAllUsersExpectCurrent(@RequestParam Long currentUserId) {
        return userRepository.findByIdNot(currentUserId);
    }

    @PostMapping("/profile/upload")
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("userId") Long userId,
                                                  @RequestParam("image") MultipartFile imageFile) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            user.setProfileImage(imageFile.getBytes());

            //disable auto-commit so PostgreSQL LargeObjectManager doesn't crash
            entityManager.unwrap(Session.class).doWork(connection -> {
                connection.setAutoCommit(false);
            });

            userRepository.save(user);

            return ResponseEntity.ok("Profile picture uploaded successfully");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading image: " + e.getMessage());
        }
    }


    @GetMapping("/profile/image")
    // fetch byte[] from DB and return it with IMAGE_JPEG header
    public ResponseEntity<byte[]> getProfileImage(@RequestParam("userId") Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getProfileImage() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(user.getProfileImage());
    }


}
