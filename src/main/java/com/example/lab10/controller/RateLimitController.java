package com.example.lab10.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/*
 * Controller for the rate limit page.
 * I use this page when a client sends too many requests.
 */
@Controller
public class RateLimitController {

    /*
     * Shows the rate-limit page.
     * The rate limit filter forwards the request here
     * when the limit is exceeded.
     */
    @RequestMapping(value = "/rate-limit", method = {RequestMethod.GET, RequestMethod.POST})
    public String rateLimit(HttpServletRequest request, Model model) {

        /*
         * The rate limit filter stores details in request attributes.
         * I read them here to show a clear message to the user.
         */
        Object status = request.getAttribute("statusCode");
        Object msg = request.getAttribute("message");
        Object retry = request.getAttribute("retryAfterSeconds");

        // HTTP status code (default is 429)
        model.addAttribute("statusCode", status != null ? status : 429);

        // Message explaining why the request was blocked
        model.addAttribute(
                "message",
                msg != null ? msg : "Too Many Requests - Please wait and try again."
        );

        // How many seconds the user should wait before retrying
        model.addAttribute("retryAfterSeconds", retry != null ? retry : 60);

        return "rate-limit";
    }
}