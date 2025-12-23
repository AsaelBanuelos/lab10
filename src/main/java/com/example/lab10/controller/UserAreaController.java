package com.example.lab10.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for user-accessible endpoints.
 * Handles pages that authenticated users with ROLE_USER or ROLE_ADMIN can access.
 *
 * Security: All methods require ROLE_USER or ROLE_ADMIN authority.
 * Spring Security enforces this via @RequestMatchers("/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
 */
@Controller
public class UserAreaController {

    /**
     * Displays the user home/dashboard page.
     *
     * This method:
     * 1. Gets the currently authenticated user from Spring Security
     * 2. Extracts the user's email address (used as username)
     * 3. Gets the user's roles/authorities (to show what permissions they have)
     * 4. Passes both to the template for display
     * 5. Returns the user home template
     *
     * Only accessible to users with ROLE_USER or ROLE_ADMIN authority.
     *
     * @param authentication The current user's authentication details (Spring Security provides this automatically)
     * @param model The model object used to pass data to the Thymeleaf template
     * @return The view name "user/home" which maps to user/home.html
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
