package com.example.lab10.security;

import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Spring Security uses this during login.
 * I load the user from the DB and convert it to UserDetails.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // injects the repository so I can fetch users from the DB
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * Spring calls this when someone tries to log in.
     * here finds the user by email and return a UserDetails object.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // I normalize the email to avoid login issues (spaces / uppercase)
        String normalized = username.trim().toLowerCase();

        // I load the user from DB (if not found -> login fails)
        User user = userRepository.findByEmail(normalized)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + normalized)
                );


//        DEBUG:
//        System.out.println("LOAD USER -> found=" + user.getEmail());
//        System.out.println("LOAD USER -> storedHash=" + user.getPassword());
//        System.out.println("LOAD USER -> role=" + user.getRole());

        //  converts my User entity into Spring Security's UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}