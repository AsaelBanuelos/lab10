package com.example.lab10.model;

import jakarta.persistence.*;

/*
 * JPA entity representing an application user.
 *
 * This entity stores authentication and authorization data
 * and is mapped to the "users" table in the database.
 */
@Entity
@Table(name = "users")
public class User {

    /*
     * Primary key of the user.
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*
     * Username used by Spring Security.
     * the username is the user's email.
     * Must be unique.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /*
     * User email address.
     * Stored separately and also must be unique.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /*
     * User password (hashed).
     *
     * Passwords are stored as BCrypt hashes.
     * Plain text passwords are NEVER stored.
     */
    @Column(nullable = false)
    private String password;

    /*
     * User role used for authorization.
     *
     * Examples:
     * - ROLE_USER (default)
     * - ROLE_ADMIN
     */
    @Column(nullable = false)
    private String role = "ROLE_USER";

    /*
     * Default constructor required by JPA.
     */
    public User() {
    }

    /*
     * Constructor used when creating a new user.
     */
    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /*
     * Getter for user ID.
     */
    public Integer getId() {
        return id;
    }

    /*
     * Getter for username.
     */
    public String getUsername() {
        return username;
    }

    /*
     * Setter for username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /*
     * Getter for email.
     */
    public String getEmail() {
        return email;
    }

    /*
     * Setter for email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * Getter for the hashed password.
     */
    public String getPassword() {
        return password;
    }

    /*
     * Setter for the hashed password.
     * This should only be called with an already-hashed value.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /*
     * Getter for user role.
     */
    public String getRole() {
        return role;
    }

    /*
     * Setter for user role.
     */
    public void setRole(String role) {
        this.role = role;
    }
}