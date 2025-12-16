package com.example.lab10.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTitleValidator implements ConstraintValidator<ValidTitle, String> {

    // Custom validation logic for the @ValidTitle annotation
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // Let @NotBlank handle null or empty values
        if (value == null) {
            return true;
        }

        // Check that the title has at least 3 characters
        return value.trim().length() >= 3;
    }
}
