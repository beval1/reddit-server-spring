package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class CloudinaryException extends ApiException{
    public CloudinaryException(HttpStatus status, String message) {
        super(status, message);
    }
}
