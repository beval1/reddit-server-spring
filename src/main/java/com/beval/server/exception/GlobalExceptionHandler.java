package com.beval.server.exception;

import com.beval.server.dto.response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    //default handler for all exceptions but spring default handler does a good job

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

    @ExceptionHandler({ResourceNotFoundException.class, ResourceArchivedException.class, RoleNotFoundException.class,
            UserAlreadyExistsException.class, NotAuthorizedException.class, CloudinaryException.class})
    public ResponseEntity<Object> handleNotFound(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(
                        ResponseDTO
                                .builder()
                                .message(ex.getMessage())
                                .content(null)
                                .build()
                );
    }

    //@PreAuthorize handler
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(
                        ResponseDTO
                                .builder()
                                .message(ex.getMessage())
                                .content(null)
                                .build()
                );
    }

}
