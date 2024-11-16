package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.*;
import com.sustech.cs309.project.sussycourses.repository.CourseStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.RoleRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebAppUserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final WebAppUserRepository webAppUserRepository;
    private final RoleRepository roleRepository;
    private final CourseStudentRepository courseStudentRepository;

    private final EmailService emailService;

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
        webAppUser.setRole(roleRepository.findById(registrationDto.roleId()).orElse(null));
        webAppUser.setEnabled(false);
        webAppUser.setVerificationToken(UUID.randomUUID().toString());
        webAppUser.setGender(registrationDto.gender());
        webAppUser.setCreatedAt(LocalDateTime.now());

        webAppUserRepository.save(webAppUser);

        String recipientAddress = webAppUser.getEmail();
        String subject = "SUSSYCourses: Confirm Your Email Address";
        String confirmationUrl = "http://localhost:8080/verify-email?verificationToken=" + webAppUser.getVerificationToken();
        String message = "Dear " + webAppUser.getFullName() + ",\n\n"
                + "Thank you for registering with SUSSYCourses. To complete your registration, please confirm your email address by clicking the link below:\n\n"
                + confirmationUrl + "\n\n"
                + "If you did not create an account with us, please disregard this email. For assistance, you can reach our support team.\n\n"
                + "Best regards,\n"
                + "The SUSSYCourses Team";

        emailService.sendEmail(recipientAddress, subject, message);

        return ResponseEntity.status(201).body("Our dear user, please check your email to verify your account. Thank you for joining us.");
    }

    public ResponseEntity<String> loginWebAppUser(@RequestBody LoginDto loginDto) {
        try {
            // Create an authentication token with the provided email and password
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDto.email(), loginDto.password()
            );

            // Authenticate using AuthenticationProvider
            Authentication authentication = authenticationProvider.authenticate(authenticationToken);

            // Log the authentication success and user details
            if (authentication.isAuthenticated()) {
                log.info("Authentication successful for email: {}", loginDto.email());
                log.info("Authenticated user: {}", authentication.getPrincipal()); // Logs the UserDetails object
//                log.info("User roles: {}", userDetails.getAuthorities()); // Logs the roles of the user
// Check if userDetails is not null and has authorities
                String scope = authentication.getAuthorities()
                        .stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .collect(Collectors.joining(" "));
                log.info("Scope: {}", scope);

            } else {
                log.warn("Authentication failed for email: {}", loginDto.email());
            }

            // Return a successful response if authentication is successful
            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException e) {
            // Log an error if authentication fails
            log.error("Authentication failed for email: {}. Error: {}", loginDto.email(), e.getMessage());
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    public String validateVerificationToken(String verificationToken) {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByVerificationToken(verificationToken);
        if (webAppUserOptional.isEmpty() || webAppUserOptional.get().isEnabled()) {
            return "invalid";
        }

        WebAppUser webAppUser = webAppUserOptional.get();
        webAppUser.setEnabled(true);
        webAppUserRepository.save(webAppUser);

        return "valid";
    }

    public List<UserResponse> findAllUser() {
        List<WebAppUser> webAppUsers = webAppUserRepository.findAll();
        return webAppUsers.stream()
                .map(webAppUser -> new UserResponse(webAppUser.getUserId(), webAppUser.getFullName(), webAppUser.getEmail(), webAppUser.getProfilePicture(), webAppUser.getGender(), webAppUser.getRole().getRoleName(), webAppUser.getBio(), webAppUser.getCreatedAt()))
                .toList();
    }

    public StudentDetailResponse getStudentById(long userId) {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findById(userId);
        if (webAppUserOptional.isEmpty()) {
            return null;
        }

        WebAppUser webAppUser = webAppUserOptional.get();
        List<CourseStudent> courses = courseStudentRepository.findEnrolledCourseStudentsByStudentId(userId);
        List<CourseInfoResponse> courseInfoResponses = courses.stream()
                .map(courseStudent -> new CourseInfoResponse(courseStudent.getCourse().getCourseId(),
                        courseStudent.getCourse().getCourseName(), courseStudent.getCourse().getDescription(), courseStudent.getCourse().getTopic(),
                        courseStudent.getCourse().getCoverImage(), courseStudent.getCourse().getTeacher().getUserId(),
                        courseStudent.getCourse().getTeacher().getFullName(), courseStudent.getCourse().getType(),
                        courseStudent.getStatus(), courseStudent.isLiked()))
                .toList();

        return new StudentDetailResponse(
                userId,
                webAppUser.getFullName(),
                webAppUser.getEmail(),
                webAppUser.getProfilePicture(),
                webAppUser.getGender(),
                webAppUser.getRole().getRoleName(),
                webAppUser.getPoints(),
                webAppUser.getBio(),
                webAppUser.getCreatedAt(),
                courseInfoResponses.size(),
                courseInfoResponses
        );
    }
}