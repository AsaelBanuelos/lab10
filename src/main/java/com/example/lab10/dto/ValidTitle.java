package com.example.lab10.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidTitleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTitle {

    // Default validation message when the title is too short
    String message() default "Title must be at least 3 characters long";

    // Used for grouping constraints (not used in this project)
    Class<?>[] groups() default {};

    // Can be used by clients of the Bean Validation API
    Class<? extends Payload>[] payload() default {};
}
