package com.example.lab10.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/*
 * Validator for the @ValidTitle annotation.
 * I use this to enforce custom rules for note titles.
 */
public class ValidTitleValidator implements ConstraintValidator<ValidTitle, String> {

    /*
     * This method runs automatically during validation.
     * It checks if the title follows my custom rules.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // If the value is null, I let @NotBlank handle the error
        if (value == null) {
            return true;
        }

        //tr9ms spaces and require at least 3 characters
        return value.trim().length() >= 3;
    }
}