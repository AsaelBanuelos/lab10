package com.example.lab10.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/*
 * Custom validation annotation for passwords.
 *
 * I put this on DTO fields (like RegisterRequest.password)
 * so Spring/Jakarta Validation will automatically call my
 * PasswordPolicyValidator during registration.
 */
@Documented
@Constraint(validatedBy = PasswordPolicyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    // Default message shown when the password fails the policy checks
    String message() default "Password does not meet security policy";

    // Not used in this project, but required by the validation API
    Class<?>[] groups() default {};

    // Extra metadata (also required by the validation API)
    Class<? extends Payload>[] payload() default {};
}