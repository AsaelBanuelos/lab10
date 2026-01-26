package com.example.lab10.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Controller for pages accessible to logged-in users.
 * I allow both USER and ADMIN roles to access this area.
 * The role checks are done in SecurityConfig, not here.
 */
@Controller
public class UserAreaController {

    /*
     * Shows the user dashboard page.
     * This method is only called if the user is authenticated
     * and has the correct role.
     */
    @GetMapping("/user")
    public String userHome(Authentication authentication, Model model) {

        // gets the logged-in user's email/username
        model.addAttribute("email", authentication.getName());

        // shows the user's roles (USER / ADMIN)
        model.addAttribute("authorities", authentication.getAuthorities());

        return "user/home";
    }
}