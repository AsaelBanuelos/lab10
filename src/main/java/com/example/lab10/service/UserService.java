package com.example.lab10.service;

import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service layer for user management and authentication operations.
 * Handles user registration, password hashing, and user-related business logic.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor injection for dependencies.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user account with a hashed password.

     * Registration process:
     * 1. Normalize email (trim whitespace, convert to lowercase)
     * 2. Hash the password using BCrypt
     * 3. Create User entity with normalized email and hashed password
     * 4. Set default role to ROLE_USER
     * 5. Save to database

     * Security features:
     * - Password is hashed with BCrypt (strength 12)
     * - Email normalization prevents duplicate accounts with different casing
     * - Username and email are set to the same value
     */
    public User register(String email, String rawPassword) {
        // Normalize email to avoid case-sensitivity issues and whitespace problems
        String normalizedEmail = email.trim().toLowerCase();

        // Hash the password using BCrypt
        // The encoder automatically generates a unique salt for each password
        String hashed = passwordEncoder.encode(rawPassword);

        // Debug logs: Track registration and verify password hashing
//        System.out.println("REGISTER -> email=" + "[" + normalizedEmail + "]" + ", hashed=" + hashed);
//        System.out.println("ENCODER TEST -> matches? " + passwordEncoder.matches(rawPassword, hashed));

        // Create new user with normalized email as both username and email
        // Default role is ROLE_USER
        User user = new User(normalizedEmail, normalizedEmail, hashed, "ROLE_USER");

        // Persist the user to the database
        return userRepository.save(user);
    }
}
