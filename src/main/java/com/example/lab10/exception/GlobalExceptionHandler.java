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
 *
 * Uses @ControllerAdvice to apply exception handling across all controllers.
 * Returns error view pages with appropriate HTTP status codes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from HTML form submissions.
     * Triggered when @Valid fails on @ModelAttribute parameters.
     *
     * Common scenarios:
     * - Missing required fields
     * - Invalid email format
     * - Password too short
     * - Title/content validation failures
     *
     * @param ex The binding exception containing validation errors
     * @param model Model for passing error message to the view
     * @return Error view name
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException ex, Model model) {
        model.addAttribute("message", "Validation failed (form data).");
        return "error";
    }

    /**
     * Handles validation errors from JSON request bodies.
     * Triggered when @Valid fails on @RequestBody parameters.
     *
     * This occurs when REST API clients send invalid JSON data.
     *
     * @param ex The method argument validation exception
     * @param model Model for passing error message to the view
     * @return Error view name
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("message", "Validation failed (JSON body).");
        return "error";
    }

    /**
     * Handles requests with unsupported Content-Type headers.
     *
     * Examples:
     * - Sending JSON to an endpoint expecting form data
     * - Missing Content-Type header
     * - Incorrect media type specification
     *
     * @param ex The media type not supported exception
     * @param model Model for passing error message to the view
     * @return Error view name
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public String handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, Model model) {
        model.addAttribute("message", "Unsupported Content-Type.");
        return "error";
    }

    /**
     * Handles custom ResponseStatusException thrown in controllers.
     *
     * Controllers throw this exception to return specific HTTP status codes
     * with custom error messages (e.g., file upload failures).
     *
     * @param ex The response status exception with status code and reason
     * @param model Model for passing error message to the view
     * @return Error view name
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
     *
     * This prevents stack traces from being displayed to users
     * and provides a consistent error experience.
     *
     * @param ex The unexpected exception
     * @param model Model for passing error message to the view
     * @return Error view name
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("message", "Server error.");
        return "error";
    }
}
