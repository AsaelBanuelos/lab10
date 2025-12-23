package com.example.lab10.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration for registering HTTP interceptors.
 * This configuration adds custom security headers to all HTTP responses.
 */
@Configuration
public class SecurityHeadersConfig implements WebMvcConfigurer {

    /**
     * Registers custom interceptors to process all incoming HTTP requests.
     *
     * Interceptors allow us to:
     * - Execute code before/after controller methods
     * - Modify requests and responses
     * - Add security headers
     * - Implement cross-cutting concerns
     *
     * @param registry Registry for adding interceptors to the application
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the security headers interceptor to add CSP headers
        registry.addInterceptor(new SecurityHeadersInterceptor());
    }
}
