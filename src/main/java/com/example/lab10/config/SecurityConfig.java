package com.example.lab10.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    // This bean defines the main Spring Security configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF for simplicity (not for production)
                .csrf(csrf -> csrf.disable())

                // Configure which requests are allowed without authentication
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no login required)
                        .requestMatchers("/hello", "/notes/**").permitAll()

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )

                // Enable default Spring Security login form
                .formLogin(form -> form.permitAll());

        // Build and return the security filter chain
        return http.build();
    }
}
