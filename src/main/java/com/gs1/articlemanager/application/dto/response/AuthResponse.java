// src/main/java/com/gs1/articlemanager/application/dto/response/AuthResponse.java
package com.gs1.articlemanager.application.dto.response;

public class AuthResponse {
    private String token;
    private String type;
    private UserResponse user;

    public AuthResponse() {
        this.type = "Bearer";
    }

    public AuthResponse(String token, UserResponse user) {
        this.token = token;
        this.type = "Bearer";
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
