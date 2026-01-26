package com.example.lab10.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/*
 * Custom validation annotation used for note titles.
 *
 * This annotation is applied to String fields and
 * delegates the validation logic to ValidTitleValidator.
 */
@Documented
@Constraint(validatedBy = ValidTitleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTitle {

    /*
     * Default error message shown when the title validation fails.
     * This message can be overridden when using the annotation.
     */
    String message() default "Title must be at least 3 characters long";

    /*
     * Validation groups.
     * These allow applying different rules in different contexts.
     * Not used in this project, but required by the Bean Validation API.
     */
    Class<?>[] groups() default {};

    /*
     * Payload for additional metadata.
     * Not used here, but required by the Bean Validation specification.
     */
    Class<? extends Payload>[] payload() default {};
}