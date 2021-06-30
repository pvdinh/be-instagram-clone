package com.example.demo.response;

public class ResponseLogin extends BaseResponse{
    private String authorization;

    public ResponseLogin() {
        super();
    }

    public ResponseLogin(String authorization) {
        this.authorization = authorization;
    }

    public ResponseLogin(int statusCode, String authorization) {
        super(statusCode);
        this.authorization = authorization;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
