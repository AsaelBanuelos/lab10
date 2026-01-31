package com.example.lab10.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void protectedPage_withoutLogin_redirectsToLogin() throws Exception {

        // Try to access a protected page without authentication
        mockMvc.perform(get("/notes"))

                // User should be redirected to the login page
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string(
                        "Location",
                        org.hamcrest.Matchers.containsString("/login")
                ));
    }
}
