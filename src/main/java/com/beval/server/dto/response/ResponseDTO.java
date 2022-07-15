package com.beval.server.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private String message;
    private Object content;
    private LocalDateTime timestamp;

    public ResponseDTO(String message, Object content, LocalDateTime timestamp) {
        this.message = message;
        this.content = content;
        if(timestamp == null){
            this.timestamp = LocalDateTime.now();
        }
    }
}
