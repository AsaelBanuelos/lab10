package com.example.lab10.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * Controller for the 403 Forbidden page.
 * I use this when a user is logged in but not allowed to access a page.
 */
@Controller
public class ForbiddenController {

    /*
     * Shows the forbidden page.
     * Spring Security redirects the user here after a 403 error.
     */
    @GetMapping("/forbidden")
    public String forbidden(Model model) {

        //  shows the HTTP status code on the page
        model.addAttribute("statusCode", 403);

        //  shows a simple message explaining why access was denied
        model.addAttribute("message", "Forbidden - you don't have permission to access this page.");

        return "forbidden";
    }
}