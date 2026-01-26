package com.example.lab10.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.example.lab10.validation.ValidPassword;

/*
 * DTO (Data Transfer Object) used during user registration.
 *
 * This class defines all validation rules for creating a new user account.
 * Validation is enforced server-side before the user is saved to the database.
 */
public class RegisterRequest {

    /*
     * User email address.
     *
     * Rules:
     * - must not be empty
     * - must be a valid email format
     *
     * This value is also used as the username for login.
     */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;

    /*
     * User password.
     *
     * Rules:
     * - must not be empty
     * - minimum length of 8 characters
     * - must satisfy custom password policy (@ValidPassword)
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @ValidPassword
    private String password;

    /*
     * Getter for the user's email.
     */
    public String getEmail() {
        return email;
    }

    /*
     * Setter for the user's email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * Getter for the user's password.
     */
    public String getPassword() {
        return password;
    }

    /*
     * Setter for the user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}