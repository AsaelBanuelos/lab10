package com.example.lab10.repository;

import com.example.lab10.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
 * Repository for User entity.
 * I use this to load users from the database.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /*
     * Finds a user by username.
     * In the app, username = email.
     * Spring Security uses this during login.
     */
    Optional<User> findByUsername(String username);

    /*
     * Finds a user by email.
     *  uses this to register users and load user data safely.
     */
    Optional<User> findByEmail(String email);
}