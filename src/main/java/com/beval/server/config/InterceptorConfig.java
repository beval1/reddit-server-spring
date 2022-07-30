package com.beval.server.config;

import com.beval.server.interceptor.RequestLoggerInterceptor;
import com.beval.server.interceptor.UserLoggerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoggerInterceptor());
        registry.addInterceptor(new RequestLoggerInterceptor());
    }
}
