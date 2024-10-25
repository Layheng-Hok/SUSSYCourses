package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.LoginDto;
import com.sustech.cs309.project.sussycourses.dto.RegistrationDto;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
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

        String testSubject = "SUSSYCourses: Email Address Validation";
        String testMessage = "Dear user,\n\n"
                + "This message is part of a preliminary validation process for your email address. No further action is required, and you may disregard this message.\n\n"
                + "If you did not request an account with SUSSYCourses, please ignore this email.\n\n"
                + "Thank you,\n"
                + "The SUSSYCourses Team";

        try {
            emailService.sendEmail(registrationDto.email(), testSubject, testMessage);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid email address");
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
        String subject = "SUSSYCourses: Confirm Your Email Address";
        String confirmationUrl = "http://localhost:8081/verify-email?verificationToken=" + webAppUser.getVerificationToken();
        String message = "Dear " + webAppUser.getFullName() + ",\n\n"
                + "Thank you for registering with SUSSYCourses. To complete your registration, please confirm your email address by clicking the link below:\n\n"
                + confirmationUrl + "\n\n"
                + "If you did not create an account with us, please disregard this email. For assistance, you can reach our support team.\n\n"
                + "Best regards,\n"
                + "The SUSSYCourses Team";

        emailService.sendEmail(recipientAddress, subject, message);

        return ResponseEntity.status(201).body("Dear user, please check your email for verification");
    }

    public ResponseEntity<String> loginWebAppUser(@RequestBody LoginDto loginDto) {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByEmail(loginDto.email());

        if (webAppUserOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        WebAppUser webAppUser = webAppUserOptional.get();

        if (!webAppUser.isEnabled()) {
            return ResponseEntity.status(403).body("Account is not verified");
        }

        if (!passwordEncoder.matches(loginDto.password(), webAppUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        return ResponseEntity.ok("Login successful");
    }

    public String validateVerificationToken(String verificationToken) {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByVerificationToken(verificationToken);
        if (webAppUserOptional.isEmpty()) {
            return "invalid";
        }

        WebAppUser webAppUser = webAppUserOptional.get();
        webAppUser.setEnabled(true);
        webAppUser.setVerificationToken(null);
        webAppUserRepository.save(webAppUser);

        return "valid";
    }
}