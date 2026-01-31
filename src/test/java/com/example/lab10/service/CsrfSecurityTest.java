package com.example.lab10.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// Helpers from Spring Security Test to simulate logged users and CSRF tokens
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

// MockMvc utilities to build HTTP requests and check responses
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * This test class checks how CSRF protection works in my application.
 *
 * The idea is simple:
 * - If I send a POST request WITHOUT a CSRF token → Spring Security must block it.
 * - If I send the SAME request WITH a CSRF token → Spring Security should allow it.
 *
 * This is required by Lab 14 to prove that CSRF protection is enabled and working.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CsrfSecurityTest {

    /*
     * MockMvc allows me to simulate HTTP requests
     * without running a real browser or server.
     */
    @Autowired
    private MockMvc mockMvc;

    /*
     * This test checks the negative case:
     * POST request WITHOUT CSRF token.
     *
     * Expected behavior:
     * - Spring Security rejects the request
     * - HTTP status must be 403 Forbidden
     */
    @Test
    void postWithoutCsrf_isForbidden() throws Exception {
        mockMvc.perform(
                        post("/notes/api")                 // endpoint I want to test
                                .with(user("test@example.com").roles("USER")) // simulate logged-in user
                                .contentType(MediaType.APPLICATION_JSON)       // sending JSON
                                .content("""
                                        {
                                          "title": "Test title",
                                          "content": "Test content"
                                        }
                                        """)
                )
                .andExpect(status().isForbidden()); // CSRF should block this
    }

    /*
     * This test checks the positive case:
     * POST request WITH a CSRF token.
     *
     * Expected behavior:
     * - Request is NOT blocked by CSRF
     * - Status can be 200, 201, or 302 (redirect)
     * - The important thing is: NOT 403
     */
    @Test
    void postWithCsrf_isAccepted() throws Exception {
        mockMvc.perform(
                        post("/notes/api")
                                .with(user("test@example.com").roles("USER")) // logged-in user
                                .with(csrf())                                  // CSRF token added
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "title": "Test title",
                                          "content": "Test content"
                                        }
                                        """)
                )
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();

                    // If CSRF is working correctly, it should NOT be 403
                    if (status == 403) {
                        throw new AssertionError(
                                "Request should be allowed when CSRF token is present"
                        );
                    }
                });
    }
}
