package com.example.lab10.config;

import jakarta.servlet.http.HttpServletResponse;
import com.example.lab10.security.SimpleRateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/*
 * Main security config.
 * Here I set: login, roles, CSRF/session rules, headers, and rate limiting.
 */
@Configuration
public class SecurityConfig {

    // Blocks too many login/register attempts (basic brute-force protection)
    private final SimpleRateLimitFilter simpleRateLimitFilter;

    public SecurityConfig(SimpleRateLimitFilter simpleRateLimitFilter) {
        this.simpleRateLimitFilter = simpleRateLimitFilter;
    }

    /*
     * Connects Spring Security with my DB users.
     * Uses UserDetailsService + PasswordEncoder (BCrypt).
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

    // Needed for "max 1 session per user" feature
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // Publishes session create/destroy events (helps session limiting work correctly)
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /*
     * Main Spring Security rules.
     * This is basically "who can access what" + session/logout/headers.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SessionRegistry sessionRegistry) throws Exception {

        // If not logged in, send user to /login
        AuthenticationEntryPoint entryPoint =
                (request, response, authException) -> response.sendRedirect("/login");

        // If logged in but no permission, show 403 page (not a redirect)
        AccessDeniedHandler accessDeniedHandler = (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 visible in Network tab
            request.setAttribute("statusCode", 403);
            request.setAttribute("message", "Forbidden - you don't have permission to access this page.");
            request.getRequestDispatcher("/forbidden").forward(request, response);
        };

        http
                // Rate limiting before auth filter (so it blocks brute-force early)
                .addFilterBefore(simpleRateLimitFilter, UsernamePasswordAuthenticationFilter.class)

                // Security headers
                .headers(headers -> headers
                        .contentTypeOptions(Customizer.withDefaults()) // nosniff
                        .frameOptions(frame -> frame.deny())          // clickjacking protection
                        .referrerPolicy(ref -> ref.policy(
                                ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                )

                // Authorization rules (public vs roles)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/error", "/hello", "/headers",
                                "/rate-limit", "/forbidden", "/favicon.ico").permitAll()
                        .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user", "/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )

                // Custom 401/403 handling
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                // Form login
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler((request, response, authentication) -> {
                            // After login, redirect depending on role
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            if (isAdmin) response.sendRedirect("/admin");
                            else response.sendRedirect("/notes");
                        })
                )

                // Logout clears session + cookie
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/login?logout")
                )

                // Session hardening: new session on login + only 1 active session
                .sessionManagement(sm -> sm
                        .sessionFixation(sf -> sf.migrateSession())
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry)
                );

        return http.build();
    }
}