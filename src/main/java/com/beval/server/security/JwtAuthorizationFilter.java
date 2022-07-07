package com.beval.server.security;

import com.beval.server.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
public class JwtAuthorizationFilter implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Serverlet path: " + request.getServletPath());
        //TODO: remove path check
        if (request.getServletPath().equals("/api/v1/auth/signin")) {
            return;
        }

        response.setContentType(APPLICATION_JSON_VALUE);
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED,
                "You are not authorized!",
                "Not Authorized!");
//        Map<String, String> responseJson = new HashMap<>();
//        responseJson.put("message", "You are not authorized!");
//        responseJson.put("status", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
//        responseJson.put("error", "Not Authorized");
        new ObjectMapper().writeValue(response.getOutputStream(), apiError);
    }
}
