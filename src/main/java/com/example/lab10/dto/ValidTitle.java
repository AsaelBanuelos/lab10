package com.example.lab10.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation for note titles.
 * Ensures that titles meet minimum length requirements.
 *
 * This annotation uses ValidTitleValidator for the actual validation logic.
 * It can be applied to String fields that represent note titles.
 *
 * Usage:
 * @ValidTitle(message = "Custom error message")
 * private String title;
 */
@Documented
@Constraint(validatedBy = ValidTitleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTitle {

    /**
     * Default error message when validation fails.
     */
    String message() default "Title must be at least 3 characters long";

    /**
     * Validation groups - allows grouping constraints for different scenarios.
     * Not currently used in this application.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for clients of the Bean Validation API.
     */
    Class<? extends Payload>[] payload() default {};
}
