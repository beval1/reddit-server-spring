package com.beval.server.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserLoggerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddress = request.getHeader("X-Forward-For");
        String userAgent = request.getHeader("User-Agent");

        if(ipAddress == null){
            ipAddress = request.getRemoteAddr();
        }

        log.info(String.format("New Request from IP: %s with user-agent: %s", ipAddress, userAgent));
        return true;
    }
}
