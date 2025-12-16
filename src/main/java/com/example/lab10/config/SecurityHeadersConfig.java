package com.example.lab10.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityHeadersConfig implements WebMvcConfigurer {

    // Register custom interceptors for all incoming HTTP requests
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // This interceptor is used to add security-related HTTP headers
        registry.addInterceptor(new SecurityHeadersInterceptor());
    }
}
