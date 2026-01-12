package com.example.lab10.repository;

import com.example.lab10.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for User entity operations.

 * Provides automatic CRUD operations and custom query methods
 * for user authentication and management.

 * Spring Data JPA automatically implements methods based on their names:
 * - findByUsername - Queries by username field
 * - findByEmail - Queries by email field

 * Returns Optional<User> to safely handle cases where a user is not found.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by username.

     * In this application, username is the same as email.
     * Used by Spring Security during authentication.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by email address.

     * Used during:
     * - User registration (to check if email already exists)
     * - Login authentication
     * - User lookup operations
     */
    Optional<User> findByEmail(String email);
}
