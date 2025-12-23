package com.example.lab10.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator implementation for the @ValidTitle annotation.
 * Validates that a title string meets minimum length requirements.
 *
 * Validation logic:
 * - Null values are considered valid (let @NotBlank handle null checks)
 * - Empty strings are trimmed and checked for minimum 3 characters
 * - Leading/trailing whitespace is ignored in length calculation
 */
public class ValidTitleValidator implements ConstraintValidator<ValidTitle, String> {

    /**
     * Validates the title string according to custom rules.
     *
     * The method:
     * 1. Returns true for null values (delegates null handling to @NotBlank)
     * 2. Trims whitespace from the value
     * 3. Checks that the trimmed value has at least 3 characters
     *
     * @param value The title string to validate
     * @param context Context in which the constraint is evaluated
     * @return true if valid (null or >= 3 characters), false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Allow null values - @NotBlank will handle null validation
        if (value == null) {
            return true;
        }

        // Check that the trimmed title has at least 3 characters
        return value.trim().length() >= 3;
    }
}
