package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends ApiException{
    public RoleNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
