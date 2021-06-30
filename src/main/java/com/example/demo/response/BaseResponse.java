package com.example.demo.response;

public class BaseResponse {
    private int statusCode;

    public BaseResponse() {
        super();
    }

    public BaseResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
