package com.example.demo.response;

import java.util.List;

public class ResponseData<T> extends BaseResponse{
    private List<T> data;
    private long total;

    public ResponseData() {
        super();
    }

    public ResponseData(List<T> data) {
        this.data = data;
    }

    public ResponseData(int statusCode, List<T> data, long total) {
        super(statusCode);
        this.data = data;
        this.total = total;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
