package com.beval.server.exception;

import com.beval.server.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //TODO: add exception handlers

    //exceptions for user authentications are later caught by the authenticationEntryPoint if not here
//    @ExceptionHandler({DisabledException.class, BadCredentialsException.class,
//            LockedException.class, CredentialsExpiredException.class, AccountExpiredException.class})
//    public ResponseEntity<Object> handleDisabledException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                ResponseDto
//                        .builder()
//                        .message(ex.getMessage())
//                        .content(null)
//                        .timestamp(LocalDateTime.now())
//                        .build()
//        );
//    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ResponseDTO
                        .builder()
                        .message(ex.getMessage())
                        .content(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    //default handler
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleDefault(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                ResponseDto
//                        .builder()
//                        .message(ex.getMessage())
//                        .content(null)
//                        .timestamp(LocalDateTime.now())
//                        .build()
//        );
//    }


}
