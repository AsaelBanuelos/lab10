package com.example.lab10.controller;

import com.example.lab10.dto.RegisterRequest;
import com.example.lab10.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/*
 * Controller for auth-related pages.
 * I handle register and login pages here.
 * The actual login process is handled by Spring Security.
 */
@Controller
public class AuthController {

    // I use this service to create users and hash passwords
    private final UserService userService;

    /*
     * Constructor injection.
     * Spring injects UserService automatically.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /*
     * Shows the registration page.
     *  sends an empty DTO so Thymeleaf can bind form fields.
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    /*
     * Handles registration form submit.
     * @Valid runs all validation rules on the DTO.
     */
    @PostMapping("/register")
    public String registerSubmit(
            @Valid @ModelAttribute("registerRequest") RegisterRequest req,
            BindingResult binding
    ) {
        // If validation fails, I show the form again with errors
        if (binding.hasErrors()) {
            return "register";
        }

        /*
         *  creates the user here.
         * Password hashing and default role are handled in the service.
         */
        userService.register(req.getEmail(), req.getPassword());

        return "redirect:/login";
    }

    /*
     * Shows the custom login page.
     * Spring Security handles authentication, not this controller.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}