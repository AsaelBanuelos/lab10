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
     *
     * @param userService Service for user management operations
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the user registration form.
     * Creates an empty RegisterRequest object and binds it to the form.
     *
     * @param model Model object to pass data to the view
     * @return View name for the registration page
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    /**
     * Processes the registration form submission.
     * Validates user input and creates a new user account.
     *
     * Validation includes:
     * - Email format check
     * - Password minimum length
     * - Required field validation
     *
     * @param req The registration request DTO with user input
     * @param binding Binding result containing validation errors
     * @return Redirect to login page on success, or registration form with errors
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
     * Note: Spring Security handles the actual POST /login authentication.
     *
     * @return View name for the login page
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
