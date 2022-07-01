package com.beval.server.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessDto {
    private String message;
    private HttpStatus status;
    private Object content;
    private LocalDateTime timestamp;
}
