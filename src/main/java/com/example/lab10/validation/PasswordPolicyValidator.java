package com.example.lab10.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

/*
 * Here i validate passwords during registration.
 */
public class PasswordPolicyValidator implements ConstraintValidator<ValidPassword, String> {

    /*
     * Small blacklist of very common and weak passwords.
     */
    private static final Set<String> COMMON = Set.of(
            "password", "password1", "password123",
            "12345678", "123456789", "qwerty123",
            "admin123", "iloveyou", "welcome1"
    );

    /*
     * This method is called automatically during validation.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {

        // Reject null passwords immediately
        if (value == null) return false;

        // Trims spaces just in case the user adds them accidentally
        String pw = value.trim();

        // -----------------------------
        // Rule 1: minimum length
        // -----------------------------
        if (pw.length() < 10) {
            setMsg(ctx, "Password must be at least 10 characters long");
            return false;
        }

        // -----------------------------
        // Rule 2: block common passwords
        // -----------------------------
        String lower = pw.toLowerCase();
        if (COMMON.contains(lower)) {
            setMsg(ctx, "Password is too common");
            return false;
        }

        // -----------------------------
        // Rule 3: character complexity
        // -----------------------------
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        // Checks each character and mark what we find
        for (char c : pw.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true; // anything else counts as special
        }

        // Enforce all required character types
        if (!hasUpper) { setMsg(ctx, "Password must contain at least one uppercase letter"); return false; }
        if (!hasLower) { setMsg(ctx, "Password must contain at least one lowercase letter"); return false; }
        if (!hasDigit) { setMsg(ctx, "Password must contain at least one digit"); return false; }
        if (!hasSpecial) { setMsg(ctx, "Password must contain at least one special character"); return false; }

        // If we reach here, the password is strong enough
        return true;
    }

    /*
     * Helper method to show a custom error message.
     *
     * I use this so the user sees exactly
     * what rule failed instead of a generic message.
     */
    private void setMsg(ConstraintValidatorContext ctx, String msg) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}