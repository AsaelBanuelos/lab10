package com.example.lab10.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class RoleAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void adminPage_withUserRole_isBlocked() throws Exception {

        // Simulate a logged-in user with USER role
        mockMvc.perform(
                        get("/admin")
                                .with(user("user@test.com").roles("USER"))
                )
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();

                    // Access must be blocked: either 403 or redirect
                    if (!(status == 403 || (status >= 300 && status < 400))) {
                        throw new AssertionError(
                                "Expected access to be blocked, got status " + status
                        );
                    }
                });
    }
}
