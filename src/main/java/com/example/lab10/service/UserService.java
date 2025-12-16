package com.example.lab10.service;

import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    // Repository used to manage User entities
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Creates and saves a new user in the database
    public User createUser(String email, String password) {
        User user = new User(email, password);
        return userRepository.save(user);
    }

    // Simple authentication check using email and password
    // (For demo purposes only, passwords are not encrypted)
    public boolean authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .map(u -> u.getPassword().equals(password))
                .orElse(false);
    }
}
