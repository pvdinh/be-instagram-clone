package com.example.demo.response;

public class ResponseObject<T> extends BaseResponse{
    private T data;

    public ResponseObject(T data) {
        this.data = data;
    }

    public ResponseObject(int statusCode, T data) {
        super(statusCode);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
