package com.example.lab10.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

/**
 * Custom validator that enforces password security policies.
 * Used by the @ValidPassword annotation to validate passwords during registration.
 *
 * Security policies enforced:
 * - Minimum 10 characters long (strong length requirement)
 * - Not in common password blacklist (prevents weak passwords)
 * - Must contain uppercase letters (A-Z)
 * - Must contain lowercase letters (a-z)
 * - Must contain digits (0-9)
 * - Must contain special characters
 *
 * These requirements create complex passwords that are resistant to brute-force attacks.
 */
public class PasswordPolicyValidator implements ConstraintValidator<ValidPassword, String> {

    /**
     * Small blacklist of very common/weak passwords.
     * Blocks passwords that users might try to use despite complexity requirements.
     * In a real application, this would be much larger or use a library like HaveIBeenPwned.
     *
     * Examples blocked:
     * - password, password123 (just "password" variations)
     * - 12345678, qwerty123 (keyboard patterns)
     * - admin123, welcome1 (common default passwords)
     */
    // Small “common passwords” blacklist (enough for the lab)
    private static final Set<String> COMMON = Set.of(
            "password", "password1", "password123",
            "12345678", "123456789", "qwerty123",
            "admin123", "iloveyou", "welcome1"
    );

    /**
     * Main validation method called by Spring to check password strength.
     *
     * Validation rules are checked in order:
     * 1. Length - must be at least 10 characters
     * 2. Blacklist - cannot be a known common password
     * 3. Character types - must have uppercase, lowercase, digit, and special
     *
     * @param value The password string to validate
     * @param ctx Context for adding violation messages
     * @return true if password is valid, false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        // Reject null passwords
        if (value == null) return false;

        // Trim leading/trailing whitespace from the password
        String pw = value.trim();

        // =============================================
        // RULE 1: Check minimum length (10 characters)
        // =============================================
        if (pw.length() < 10) {
            // Set custom error message for length violation
            setMsg(ctx, "Password must be at least 10 characters long");
            return false;
        }

        // =============================================
        // RULE 2: Check against common/weak passwords
        // =============================================
        String lower = pw.toLowerCase();
        if (COMMON.contains(lower)) {
            // Set custom error message for blacklist violation
            setMsg(ctx, "Password is too common");
            return false;
        }

        // =============================================
        // RULE 3: Check for required character types
        // =============================================
        // Initialize flags to track which character types we've found
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;

        // Loop through each character in the password
        for (char c : pw.toCharArray()) {
            // Check if character is uppercase letter (A-Z)
            if (Character.isUpperCase(c)) hasUpper = true;
                // Check if character is lowercase letter (a-z)
            else if (Character.isLowerCase(c)) hasLower = true;
                // Check if character is a digit (0-9)
            else if (Character.isDigit(c)) hasDigit = true;
                // Any other character is considered special
            else hasSpecial = true;
        }

        // Check if uppercase letter requirement is met
        if (!hasUpper) { setMsg(ctx, "Password must contain at least one uppercase letter"); return false; }
        // Check if lowercase letter requirement is met
        if (!hasLower) { setMsg(ctx, "Password must contain at least one lowercase letter"); return false; }
        // Check if digit requirement is met
        if (!hasDigit) { setMsg(ctx, "Password must contain at least one digit"); return false; }
        // Check if special character requirement is met
        if (!hasSpecial) { setMsg(ctx, "Password must contain at least one special character"); return false; }

        // All rules passed - password is valid
        return true;
    }

    /**
     * Helper method to set a custom error message when validation fails.
     *
     * This method:
     * 1. Disables the default error message that Spring would use
     * 2. Replaces it with a custom message that's more specific to what failed
     * 3. Adds the violation to the validation context so Spring knows about it
     *
     * @param ctx The constraint validation context
     * @param msg The custom error message to display to the user
     */
    private void setMsg(ConstraintValidatorContext ctx, String msg) {
        // Disable the default error message from @ValidPassword annotation
        ctx.disableDefaultConstraintViolation();
        // Create a new violation with the custom message and add it to the context
        // This message will be shown to the user when validation fails
        ctx.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
