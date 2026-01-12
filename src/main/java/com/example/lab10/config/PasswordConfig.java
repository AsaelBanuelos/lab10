package com.example.lab10.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for password encoding.
 * Defines the password encoder bean used throughout the application
 * to securely hash and verify user passwords.
 */
@Configuration
public class PasswordConfig {

    /**
     * Creates a BCrypt password encoder bean.
     * BCrypt is a secure hashing algorithm designed for passwords.
     * The strength parameter (12) controls the computational cost:
     * - Higher values = more secure but slower
     * - 12 is a good balance between security and performance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
