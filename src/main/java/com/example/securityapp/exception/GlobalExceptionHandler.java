package com.example.securityapp.exception;

import com.example.securityapp.dto.ApiResponse;
import com.example.securityapp.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntime(RuntimeException ex,
                                                     HttpServletRequest request) {

        return ResponseEntity
                .badRequest()
                .body(ResponseBuilder.error(ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(ResourceNotFoundException ex,
                                                      HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseBuilder.error(
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

}