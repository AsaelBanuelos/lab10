package com.example.lab10.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.example.lab10.validation.ValidPassword;


/**
 * Data Transfer Object (DTO) for user registration.
 * Captures and validates user input during account creation.
 *
 * Validation rules:
 * - Email: Required, not blank, valid email format
 * - Password: Required, not blank, minimum 8 characters
 */
public class RegisterRequest {

    /**
     * User's email address.
     * Used for login and must be a valid email format.
     */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * User's password.
     * Must be at least 8 characters long for basic security.
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @ValidPassword
    private String password;


    /**
     * Gets the user's email address.
     *
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's password.
     *
     * @return The password (plain text, will be hashed before storage)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
