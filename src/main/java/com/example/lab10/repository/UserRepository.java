package com.example.lab10.repository;

import com.example.lab10.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository interface for User entity
// Spring Data JPA automatically implements this interface
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method to find a user by email
    // Returns Optional to handle the case where the user does not exist
    Optional<User> findByEmail(String email);
}
