package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.RegistrationDto;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class WebAppUserService {
    @Autowired
    private WebAppUserRepository webAppUserRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<String> createWebAppUser(@RequestBody RegistrationDto registrationDto) {
        if (webAppUserRepository.findByEmail(registrationDto.email()).isPresent()) {
            return ResponseEntity.status(409).body("User already exists");
        }

        WebAppUser webAppUser = new WebAppUser();

        webAppUser.setFullName(registrationDto.fullName());
        webAppUser.setEmail(registrationDto.email());
        webAppUser.setPassword(passwordEncoder.encode(registrationDto.password()));
        webAppUser.setRole(registrationDto.role());
        webAppUser.setEnabled(false);
        webAppUser.setVerificationToken(UUID.randomUUID().toString());

        webAppUserRepository.save(webAppUser);

        String recipientAddress = webAppUser.getEmail();
        String subject = "Email Verification";
        String confirmationUrl = "http://localhost:8081/verify-email?verificationToken=" + webAppUser.getVerificationToken();
        String message = "Click the link to verify your email: " + confirmationUrl;

        emailService.sendEmail(recipientAddress, subject, message);

        return ResponseEntity.status(201).body("User created but not yet verified");
    }

    public String validateVerificationToken(String verificationToken) {
        WebAppUser webAppUser = webAppUserRepository.findByVerificationToken(verificationToken).orElse(null);
        if (webAppUser == null) {
            return "invalid";
        }

        webAppUser.setEnabled(true);
        webAppUserRepository.save(webAppUser);
        return "valid";
    }
}