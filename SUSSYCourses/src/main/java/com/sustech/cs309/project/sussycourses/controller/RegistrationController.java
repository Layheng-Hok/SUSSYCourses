package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    private WebAppUserRepository webAppUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> createWebAppUser(@RequestBody WebAppUser webAppUser) {
        if (webAppUserRepository.findByEmail(webAppUser.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body("User already exists");
        }

        webAppUser.setPassword(passwordEncoder.encode(webAppUser.getPassword()));
        webAppUserRepository.save(webAppUser);

        return ResponseEntity.ok("User registered successfully");
    }
}
