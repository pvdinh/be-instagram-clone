package com.example.demo.response;

import java.util.List;

public class ResponseData<T> extends BaseResponse{
    private List<T> data;

    public ResponseData() {
        super();
    }

    public ResponseData(List<T> data) {
        this.data = data;
    }

    public ResponseData(int statusCode, List<T> data) {
        super(statusCode);
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
