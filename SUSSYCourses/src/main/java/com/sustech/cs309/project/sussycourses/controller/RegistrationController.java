package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.event.OnRegistrationCompleteEvent;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.service.EmailService;
import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RegistrationController {
    @Autowired
    private WebAppUserRepository webAppUserRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WebAppUserService webAppUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @PostMapping("/register")
    public ResponseEntity<String> createWebAppUser(@RequestBody WebAppUser webAppUser) {
        if (webAppUserRepository.findByEmail(webAppUser.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body("User already exists");
        }

        webAppUser.setPassword(passwordEncoder.encode(webAppUser.getPassword()));
        webAppUser.setEnabled(false);
        webAppUser.setVerificationToken(UUID.randomUUID().toString());

        webAppUserRepository.save(webAppUser);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(webAppUser));

        return ResponseEntity.status(201).body("User created but not yet verified");
    }
}
