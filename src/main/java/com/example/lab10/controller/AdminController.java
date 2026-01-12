package com.example.lab10.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for admin-only endpoints.
 * Handles pages that only users with ROLE_ADMIN can access.

 * Security: All methods in this controller require ROLE_ADMIN authority.
 * The Spring Security configuration enforces this via request matchers for "/admin" and "/admin/**".
 */
@Controller
public class AdminController {

    /**
     * Displays the admin dashboard/panel page.
     *
     * This method:
     * 1. Gets the currently authenticated user from Spring Security
     * 2. Extracts the user's email address
     * 3. Passes it to the admin panel template for display
     * 4. Returns the admin template
     *
     * Only accessible to users with ROLE_ADMIN authority.
     */
    @GetMapping("/admin")
    public String adminHome(Authentication authentication, Model model) {
        // Get the logged-in user's email (authentication.getName() returns the username/email)
        model.addAttribute("email", authentication.getName());

        // Return the admin panel template
        return "admin/panel";
    }
}
