package com.example.lab10.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * HTTP interceptor that adds security-related headers to all responses.
 * This helps protect against common web vulnerabilities.
 */
public class SecurityHeadersInterceptor implements HandlerInterceptor {

    /**
     * Executes before the controller handles the request.
     * Adds Content Security Policy (CSP) header to restrict resource loading.
     *
     * CSP helps prevent:
     * - Cross-Site Scripting (XSS) attacks
     * - Data injection attacks
     * - Unauthorized resource loading
     *
     * The policy "default-src 'self'" means:
     * - Only resources from the same origin can be loaded
     * - External scripts, styles, images, etc. are blocked
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        // Add Content Security Policy header to every response
        response.setHeader(
                "Content-Security-Policy",
                "default-src 'self'"  // Restrict all content to same origin
        );

        // Return true to allow the request to proceed to the controller
        return true;
    }
}
