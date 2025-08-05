//package com.example.springapi.Controller;
//
//import com.example.springapi.model.User;
//import com.example.springapi.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.IOException;
//import java.util.Arrays;
//
//
//@RestController
//@RequestMapping("/api/profile")
//public class ProfileController {
//    @Autowired
//    private UserRepository userRepository;
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadProfilePicture(@RequestParam("userId") Long userId,
//                                                  @RequestParam("image") MultipartFile image) {
//        try {
//            // 1. Find the user
//            User user = userRepository.findById(userId).orElse(null);
//            if (user == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//
//            // 2. Save image bytes directly into the database
//            user.setProfilePicturePath(Arrays.toString(image.getBytes()));  // this is the byte[] fieldD
//            userRepository.save(user);
//
//            return ResponseEntity.ok("Profile picture uploaded to database");
//
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
//        }
//    }
//    @GetMapping("/image")
//    public ResponseEntity<byte[]> getProfileImage(@RequestParam("userId") Long userId) {
//        User user = userRepository.findById(userId).orElse(null);
//        if (user == null || user.getProfileImage() == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.IMAGE_JPEG) // or IMAGE_PNG depending on what you upload
//                .body(user.getProfileImage());
//    }
//
//
//}