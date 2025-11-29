package com.example.securityapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private String status;     // SUCCESS / ERROR
    private String message;
    private Object data;
    private String path;
    private String timestamp;
}