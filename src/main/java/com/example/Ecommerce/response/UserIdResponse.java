package com.example.Ecommerce.response;

public class UserIdResponse {
    String message;
    String jwt;

    public UserIdResponse(String message, String jwt) {
        this.message = message;
        this.jwt=jwt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
