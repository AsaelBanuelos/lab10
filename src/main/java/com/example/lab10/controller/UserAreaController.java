package com.example.lab10.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for user-accessible endpoints.
 * Handles pages that authenticated users with ROLE_USER or ROLE_ADMIN can access.
 * Security: All methods require ROLE_USER or ROLE_ADMIN authority.
 */
@Controller
public class UserAreaController {

    /**
     * Displays the user home/dashboard page.
     */
    @GetMapping("/user")
    public String userHome(Authentication authentication, Model model) {
        // Get the logged-in user's email address
        model.addAttribute("email", authentication.getName());

        // Get the user's authorities/roles (example: ROLE_USER, ROLE_ADMIN)
        model.addAttribute("authorities", authentication.getAuthorities());

        // Return the user home template
        return "user/home";
    }
}
