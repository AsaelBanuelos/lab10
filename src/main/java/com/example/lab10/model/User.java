package com.example.lab10.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    // Primary key for the user table
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User email, must be unique and not null
    @Column(nullable = false, unique = true)
    private String email;

    // User password (stored as plain text for demo purposes only)
    @Column(nullable = false)
    private String password;

    // Default constructor required by JPA
    public User() {
    }

    // Constructor used to create a new user
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter for user ID
    public Long getId() {
        return id;
    }

    // Getter for user email
    public String getEmail() {
        return email;
    }

    // Setter for user email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for user password
    public String getPassword() {
        return password;
    }

    // Setter for user password
    public void setPassword(String password) {
        this.password = password;
    }
}
