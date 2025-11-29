package com.example.securityapp.utils;

import com.example.securityapp.dto.ApiResponse;

import java.time.Instant;

public class ResponseBuilder {

    public static ApiResponse success(String message, Object data, String path) {
        return ApiResponse.builder()
                .status("SUCCESS")
                .message(message)
                .data(data)
                .path(path)
                .timestamp(Instant.now().toString())
                .build();
    }

    public static ApiResponse error(String message, String path) {
        return ApiResponse.builder()
                .status("ERROR")
                .message(message)
                .data(null)
                .path(path)
                .timestamp(Instant.now().toString())
                .build();
    }

    public static ApiResponse error(String message, Object data, String path) {
        return ApiResponse.builder()
                .status("ERROR")
                .message(message)
                .data(data)
                .path(path)
                .timestamp(Instant.now().toString())
                .build();
    }
}
