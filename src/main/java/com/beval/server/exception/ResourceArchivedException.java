package com.beval.server.exception;

import org.springframework.http.HttpStatus;

public class ResourceArchivedException extends ApiException {
    public ResourceArchivedException() {
        super(HttpStatus.FORBIDDEN, "Resource is archived");
    }

    public ResourceArchivedException(HttpStatus status, String message) {
        super(status, message);
    }
}
