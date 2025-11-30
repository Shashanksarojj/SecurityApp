package com.example.securityapp.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String msg) { super(msg); }
}
