package com.beval.server.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ApiError {
    private Timestamp timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;

    private ApiError(HttpStatus status, String message){
        this.message = message;
        this.status = status;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this(status, message);
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        this(status, message);
        errors = List.of(error);
    }
}
