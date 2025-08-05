//package com.example.chatapplication;
//
//public class LoginResponse {
//    private boolean success;
//    private String message;
//    private String token;
//
//    // You can also include other fields like user ID, name, etc.
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public String getToken() {
//        return token;
//    }
//}
package com.example.chatapplication;

public class LoginResponse {
    private boolean success;
    private String message;
    private Long id;         // ✅ new
    private String username; // ✅ new
    private String email;    // ✅ new

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}

