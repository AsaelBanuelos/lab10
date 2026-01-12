package com.example.lab10.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Custom validator implementation for the @ValidTitle annotation.
 * Validates that a title string meets minimum length requirements.
 */
public class ValidTitleValidator implements ConstraintValidator<ValidTitle, String> {

    /**
     * Validates the title string according to custom rules.
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
