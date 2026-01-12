package com.example.lab10.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global exception handler for the entire application.
 * Catches exceptions thrown by controllers and provides consistent error responses.
 * Uses @ControllerAdvice to apply exception handling across all controllers.
 * Returns error view pages with appropriate HTTP status codes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors:
     * - Missing required fields
     * - Invalid email format
     * - Password too short
     * - Title/content validation failures
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException ex, Model model) {
        model.addAttribute("message", "Validation failed (form data).");
        return "error";
    }

    /**
     * Handles validation errors from JSON request bodies.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("message", "Validation failed (JSON body).");
        return "error";
    }

    /**
     * Handles requests with unsupported Content-Type headers
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public String handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, Model model) {
        model.addAttribute("message", "Unsupported Content-Type.");
        return "error";
    }

    /**
     * Handles custom ResponseStatusException thrown in controllers.
     * Controllers throw this exception to return specific HTTP status codes
     * with custom error messages
     */
    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatus(
            ResponseStatusException ex,
            jakarta.servlet.http.HttpServletResponse response,
            Model model
    ) {
        response.setStatus(ex.getStatusCode().value());
        model.addAttribute("message", ex.getReason());
        return "error";
    }


    /**
     * Generic fallback exception handler for unexpected errors.
     * Catches any exception not handled by more specific handlers.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("message", "Server error.");
        return "error";
    }
}
