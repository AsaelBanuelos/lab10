package com.example.lab10.service;

import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
 * Service for user-related logic.
 * I mainly use this for user registration and password hashing.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // I inject the repository and the password encoder
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * Registers a new user.
     */
    public User register(String email, String rawPassword) {

        // I normalize the email to avoid duplicates with different casing
        String normalizedEmail = email.trim().toLowerCase();

        /*
         *Hashes the password using BCrypt.
         */
        String hashed = passwordEncoder.encode(rawPassword);

        /*
         * Debug:
         * I keep these commented out to avoid logging sensitive data.
         */
//        System.out.println("REGISTER -> email=" + normalizedEmail + ", hashed=" + hashed);
//        System.out.println("ENCODER TEST -> matches? " + passwordEncoder.matches(rawPassword, hashed));

        /*
         *creates the user entity.
         */
        User user = new User(normalizedEmail, normalizedEmail, hashed, "ROLE_USER");

        return userRepository.save(user);
    }
}