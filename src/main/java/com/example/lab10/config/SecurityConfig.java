package com.example.lab10.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Main security configuration for the application.
 * Configures Spring Security authentication and authorization rules.
 */
@Configuration
public class SecurityConfig {

    /**
     * Creates a DAO-based authentication provider.
     *
     * This provider bridges Spring Security with our custom user authentication:
     * 1. UserDetailsService - loads user data from the database
     * 2. PasswordEncoder - verifies passwords using BCrypt hashing
     */
    @Bean
    public DaoAuthenticationProvider authProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Configures the security filter chain with authentication and authorization rules.
     *
     * Security features configured:
     * - CSRF protection (enabled by default)
     * - Custom authentication provider
     * - URL-based authorization rules
     * - Form-based login with custom page
     * - Logout functionality
     *
     * Authorization rules (in order):
     * 1. Public paths - no authentication required:
     *    - /login, /register, /error, /hello, /headers
     *
     * 2. Admin-only paths - require ROLE_ADMIN:
     *    - /admin and /admin/** (admin panel and all admin endpoints)
     *
     * 3. User-accessible paths - require ROLE_USER or ROLE_ADMIN:
     *    - /user and /user/** (user home and user endpoints)
     *    - /notes/** (note management) - covered by authenticated() below, because notes are for any logged-in user
     *
     * 4. Everything else - requires authentication (logged in):
     *    - Any other path requires user to be authenticated
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public
                        .requestMatchers("/login", "/register", "/error", "/hello", "/headers").permitAll()

                        // cover BOTH "/admin" and "/admin/**"
                        .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")

                        //  cover BOTH "/user" and "/user/**"
                        .requestMatchers("/user", "/user/**").hasAnyRole("USER", "ADMIN")

                        // Everything else
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()

                        // this avoids redirecting to "/" (server error) and redirects based on role
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            if (isAdmin) {
                                response.sendRedirect("/admin");
                            } else {
                                response.sendRedirect("/notes");
                            }
                        })
                )
                .logout(logout -> logout.logoutUrl("/logout"));

        return http.build();
    }

}
