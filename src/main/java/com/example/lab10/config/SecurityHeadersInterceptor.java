package com.example.lab10.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class SecurityHeadersInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        // Adds  a basic Content Security Policy (CSP) header to every response
        response.setHeader(
                "Content-Security-Policy",
                // This restricts content to be loaded only from the same origin
                "default-src 'self'"
        );

        return true;
    }
}
