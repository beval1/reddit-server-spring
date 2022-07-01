package com.beval.server.dto.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorDto {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private Exception error;
    private String message;
    private String path;
}
