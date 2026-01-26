package com.example.lab10.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Controller for admin pages.
 * I only allow users with ADMIN role to access this.
 * The role check is done in SecurityConfig.
 */
@Controller
public class AdminController {

    /*
     * Shows the admin dashboard.
     * This method runs only if the user is already authorized.
     */
    @GetMapping("/admin")
    public String adminHome(Authentication authentication, Model model) {

        //  gets the logged-in admin's email/username
        model.addAttribute("email", authentication.getName());


        return "admin/panel";
    }
}