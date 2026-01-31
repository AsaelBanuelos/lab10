package com.example.lab10.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/*
 * Global exception handler for the whole application
 * This class catches exceptions thrown by controllers
 * and converts them into safe, user-friendly error responses
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Handles validation errors from HTML form submissions
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException ex, Model model) {

        // Generic message to avoid exposing internal details
        model.addAttribute("message", "Validation failed (form data).");
        return "error";
    }

    /*
     * Handles validation errors from JSON request bodies
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Model model) {


        model.addAttribute("message", "Validation failed (JSON body).");

        return "error";
    }

    /*
     * Handles requests with unsupported Content-Type headers
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public String handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, Model model) {

        model.addAttribute("message", "Unsupported Content-Type.");

        return "error";
    }

    /*
     * Handles ResponseStatusException thrown manually in controllers
     */
    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatus(
            ResponseStatusException ex,
            jakarta.servlet.http.HttpServletResponse response,
            Model model
    ) {
        // Set the HTTP status explicitly
        response.setStatus(ex.getStatusCode().value());

        model.addAttribute("message", ex.getReason());

        return "error";
    }

    /*
     * Fallback handler for any unexpected exception
     * This prevents stack traces from being shown to the user
     * and returns a generic server error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex, Model model) {

        model.addAttribute("message", "Server error.");

        return "error";
    }
}