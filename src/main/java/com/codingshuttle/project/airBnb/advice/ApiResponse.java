package com.codingshuttle.project.airBnb.advice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private ApiError error;
    private T data;
    private LocalDateTime timeStamp;

    public ApiResponse(){
        this.timeStamp=LocalDateTime.now();
    }

    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }

    public ApiResponse(T data) {
       this();
        this.data = data;
    }
}
