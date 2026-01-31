package com.example.lab10.service;

import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // Mock dependencies used by UserService
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    // Service under test
    @InjectMocks
    UserService userService;

    @Test
    void register_normalizesEmail_hashesPassword_andSavesUser() {

        // Input values
        String inputEmail = "  TeSt@Example.COM  ";
        String normalizedEmail = "test@example.com";
        String rawPassword = "User12345!";
        String hashed = "$2a$12$fakebcrypt";

        // Mock password hashing
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashed);

        // Return the same user when saving
        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Call service method
        User saved = userService.register(inputEmail, rawPassword);

        // Verify password was encoded
        verify(passwordEncoder).encode(rawPassword);

        // Capture saved user
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User u = captor.getValue();

        // Check normalized email and username
        assertEquals(normalizedEmail, u.getEmail());
        assertEquals(normalizedEmail, u.getUsername());

        // Check password is hashed
        assertEquals(hashed, u.getPassword());
        assertNotEquals(rawPassword, u.getPassword());

        // Check default role
        assertEquals("ROLE_USER", u.getRole());

        // Check returned user
        assertEquals(normalizedEmail, saved.getEmail());
        assertEquals(hashed, saved.getPassword());
    }
}
