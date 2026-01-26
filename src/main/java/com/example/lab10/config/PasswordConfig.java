package com.example.lab10.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Config class for password hashing.
 * I define here how passwords are encoded before saving them.
 */
@Configuration
public class PasswordConfig {

    /*
     * BCrypt password encoder bean.
     * I use BCrypt because it is designed for password security.
     * Strength 12 gives a good balance between security and performance.
     *
     * Spring injects this bean wherever a PasswordEncoder is needed.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}