package com.example.demo.response;

public class JwtResponse {
    private String authorization;

    public JwtResponse() {
        super();
    }

    public JwtResponse(String authorization) {
        this.authorization = authorization;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
