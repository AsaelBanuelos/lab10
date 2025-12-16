package com.example.lab10.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles validation errors coming from HTML forms (@ModelAttribute)
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException ex, Model model) {
        model.addAttribute("message", "Validation failed (form data).");
        return "error";
    }

    // Handles validation errors for JSON requests (@RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Model model) {
        model.addAttribute("message", "Validation failed (JSON body).");
        return "error";
    }

    // Handles unsupported media types (for example, wrong Content-Type)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public String handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex, Model model) {
        model.addAttribute("message", "Unsupported Content-Type.");
        return "error";
    }

    // Generic fallback for unexpected server errors
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("message", "Server error.");
        return "error";
    }

    // Handles custom ResponseStatusException thrown in controllers
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleResponseStatus(ResponseStatusException ex, Model model) {
        model.addAttribute("message", ex.getReason());
        return "error";
    }
}
