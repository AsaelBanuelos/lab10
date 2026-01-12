package com.example.lab10.controller;

import com.example.lab10.dto.RegisterRequest;
import com.example.lab10.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication-related endpoints.
 * Handles user registration and login page display.
 */
@Controller
public class AuthController {

    private final UserService userService;

    /**
     * Constructor injection for UserService dependency.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the user registration form.
     * Creates an empty RegisterRequest object and binds it to the form.
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    /**
     * Processes the registration form submission.
     * Validates user input and creates a new user account.

     * Validation includes:
     * - Email format check
     * - Password minimum length
     * - Required field validation
     */
    @PostMapping("/register")
    public String registerSubmit(
            @Valid @ModelAttribute("registerRequest") RegisterRequest req,
            BindingResult binding
    ) {
        // If validation fails, return to the form with error messages
        if (binding.hasErrors()) {
            return "register";
        }

        // Create the user with default ROLE_USER role
        userService.register(req.getEmail(), req.getPassword());

        // Redirect to login page after successful registration
        return "redirect:/login";
    }

    /**
     * Displays the custom login page.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
