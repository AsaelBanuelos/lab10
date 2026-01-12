package com.example.lab10.security;

import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Custom UserDetailsService implementation for Spring Security.
 * Loads user data from the database during authentication.
 *
 * This service bridges the application's User entity with Spring Security's
 * UserDetails interface, allowing Spring Security to authenticate users
 * against our database.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor injection for UserRepository dependency.
     *
     * @param userRepository Repository for accessing user data
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by username for Spring Security authentication.
     *
     * This method is called automatically by Spring Security when a user
     * attempts to log in. It:
     * 1. Normalizes the username (email) to lowercase
     * 2. Queries the database for the user
     * 3. Converts the User entity to Spring Security's UserDetails
     * 4. Includes the user's role for authorization
     *
     * @param username The username (email) to load
     * @return UserDetails object containing user authentication data
     * @throws UsernameNotFoundException if the user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Normalize the username to lowercase and trim whitespace
        // This ensures consistent login behavior
        String normalized = username.trim().toLowerCase();

        // Debug log: Track authentication attempts
//        System.out.println("LOAD USER -> username=" + "[" + normalized + "]");

        // Query the database for the user by email
        User user = userRepository.findByEmail(normalized)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + normalized));

        // Debug logs: Show retrieved user information
//        System.out.println("LOAD USER -> found=" + user.getEmail());
//        System.out.println("LOAD USER -> storedHash=" + user.getPassword());
//        System.out.println("LOAD USER -> role=" + user.getRole());

        // Convert our User entity to Spring Security's UserDetails
        // This includes email, password hash, and role(s) for authorization
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
