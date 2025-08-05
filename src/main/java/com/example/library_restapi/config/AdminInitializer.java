package com.example.library_restapi.config;

import com.example.library_restapi.entity.User;
import com.example.library_restapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String username = "admin";
        String password = "12";
        String email = "admin@library.com";

        if (userRepository.findByUsername(username).isEmpty()) {
            User admin = new User();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRoles(Collections.singleton("ROLE_ADMIN"));

            userRepository.save(admin);
            System.out.println("Admin user created: " + username);
        }
    }
}