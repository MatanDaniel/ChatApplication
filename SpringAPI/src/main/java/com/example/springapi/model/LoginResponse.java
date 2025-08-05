//package com.example.springapi.model;
//
//public class LoginResponse {
//    private boolean success;
//    private String message;
//    private String token;
//
//    public LoginResponse(boolean success, String message, String token) {
//        this.success = success;
//        this.message = message;
//        this.token = token;
//    }
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
package com.example.springapi.model;

public class LoginResponse {
    private boolean success;
    private String message;
    private Long id;
    private String username;
    private String email;

    public LoginResponse(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}

