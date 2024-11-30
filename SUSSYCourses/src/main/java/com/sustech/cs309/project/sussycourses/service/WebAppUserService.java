package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.*;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.RoleRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.utils.CloudUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final CourseRepository courseRepository;


    private final EmailService emailService;

    public ResponseEntity<String> createWebAppUser(@RequestBody RegistrationRequest registrationRequest) {
        if (webAppUserRepository.findByEmail(registrationRequest.email()).isPresent()) {
            return ResponseEntity.status(409).body("User already exists");
        }

        String testSubject = "SUSSYCourses: Email Address Validation";
        String testMessage = "Dear user,\n\n"
                + "This message is part of a preliminary validation process for your email address. No further action is required, and you may disregard this message.\n\n"
                + "If you did not request an account with SUSSYCourses, please ignore this email.\n\n"
                + "Thank you,\n"
                + "The SUSSYCourses Team";

        try {
            emailService.sendEmail(registrationRequest.email(), testSubject, testMessage);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid email address");
        }

        WebAppUser webAppUser = new WebAppUser();

        webAppUser.setFullName(registrationRequest.fullName());
        webAppUser.setEmail(registrationRequest.email());
        webAppUser.setPassword(passwordEncoder.encode(registrationRequest.password()));
        webAppUser.setRole(roleRepository.findById(registrationRequest.roleId()).orElse(null));
        webAppUser.setEnabled(false);
        webAppUser.setVerificationToken(UUID.randomUUID().toString());
        webAppUser.setGender(registrationRequest.gender());
        webAppUser.setPoints(0);
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

    public ResponseEntity<String> loginWebAppUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Create an authentication token with the provided email and password
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.email(), loginRequest.password()
            );

            // Authenticate using AuthenticationProvider
            Authentication authentication = authenticationProvider.authenticate(authenticationToken);

            // Log the authentication success and user details
            if (authentication.isAuthenticated()) {
                log.info("Authentication successful for email: {}", loginRequest.email());
                log.info("Authenticated user: {}", authentication.getPrincipal()); // Logs the UserDetails object
//                log.info("User roles: {}", userDetails.getAuthorities()); // Logs the roles of the user
// Check if userDetails is not null and has authorities
                String scope = authentication.getAuthorities()
                        .stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .collect(Collectors.joining(" "));
                log.info("Scope: {}", scope);

            } else {
                log.warn("Authentication failed for email: {}", loginRequest.email());
            }

            // Return a successful response if authentication is successful
            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException e) {
            // Log an error if authentication fails
            log.error("Authentication failed for email: {}. Error: {}", loginRequest.email(), e.getMessage());
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

    public List<UserResponse> findAllUsers() {
        List<WebAppUser> webAppUsers = webAppUserRepository.findAll();
        return webAppUsers.stream()
                .map(webAppUser -> new UserResponse(webAppUser.getUserId(), webAppUser.getFullName(), webAppUser.getEmail(), webAppUser.getProfilePicture(), webAppUser.getGender(), webAppUser.getRole().getRoleName(), webAppUser.getBio(), webAppUser.getCreatedAt()))
                .toList();
    }

    public UserResponse findByEmail(String email) {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByEmail(email);
        if (webAppUserOptional.isEmpty() || !webAppUserOptional.get().isEnabled()
                || webAppUserOptional.get().getRole().getRoleId() == 4) {
            return null;
        }

        WebAppUser webAppUser = webAppUserOptional.get();
        return new UserResponse(
                webAppUser.getUserId(),
                webAppUser.getFullName(),
                webAppUser.getEmail(),
                webAppUser.getProfilePicture(),
                webAppUser.getGender(),
                webAppUser.getRole().getRoleName(),
                webAppUser.getBio(),
                webAppUser.getCreatedAt()
        );
    }

    public StudentDetailResponse getStudentUserDetail(Long userId) throws IOException {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByUserIdAndRoleRoleId(userId, 2);
        if (webAppUserOptional.isEmpty() || !webAppUserOptional.get().isEnabled()) {
            return null;
        }
        WebAppUser webAppUser = webAppUserOptional.get();
        return new StudentDetailResponse(
                userId,
                webAppUser.getFullName(),
                webAppUser.getEmail(),
                CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(
                        userId,
                        webAppUser.getProfilePicture())),
                webAppUser.getGender(),
                webAppUser.getRole().getRoleName(),
                webAppUser.getPoints(),
                webAppUser.getBio(),
                webAppUser.getCreatedAt()
        );
    }

    public InstructorDetailResponse getInstructorById(Long userId) throws IOException {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByUserIdAndRoleRoleId(userId, 3);

        if (webAppUserOptional.isEmpty() || !webAppUserOptional.get().isEnabled()) {
            return null;
        }

        WebAppUser webAppUser = webAppUserOptional.get();
        List<Course> courses = courseRepository.findByTeacher_UserId(userId);
        List<InstructorCourseDetailResponse> courseInfoResponses = courses.stream()
                .map(course -> {
                    try {
                        return new InstructorCourseDetailResponse(course.getCourseId(),
                                course.getCourseName(),
                                course.getDescription(),
                                course.getTopic(),
                                CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(
                                        course.getCourseId(),
                                        course.getCoverImage())),
                                course.getType(),
                                course.getStatus(),
                                course.getCreatedAt());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return new InstructorDetailResponse(
                userId,
                webAppUser.getFullName(),
                webAppUser.getEmail(),
                CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(
                        userId,
                        webAppUser.getProfilePicture())),
                webAppUser.getGender(),
                webAppUser.getRole().getRoleName(),
                webAppUser.getBio(),
                webAppUser.getCreatedAt(),
                courseInfoResponses.size(),
                courseInfoResponses
        );
    }

    public ResponseEntity<String> updateUserProfile(Long userId, UpdateUserRequest updateUserRequest) throws Exception {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByUserId(userId);
        if (webAppUserOptional.isEmpty() || !webAppUserOptional.get().isEnabled()) {
            return ResponseEntity.status(404).body("User not found or account is not enabled");
        }

        String fullName = updateUserRequest.fullName();
        String gender = updateUserRequest.gender();
        String bio = updateUserRequest.bio();
        String profilePictureName = updateUserRequest.profilePictureName();
        String fileType = updateUserRequest.fileType();
        MultipartFile profilePicture = updateUserRequest.profilePicture();

        WebAppUser webAppUser = webAppUserOptional.get();
        if (!webAppUser.getFullName().equals(fullName)) {
            webAppUser.setFullName(fullName);
        }
        if (!webAppUser.getGender().equals(gender)) {
            webAppUser.setGender(gender);
        }
        if (!webAppUser.getBio().equals(bio)) {
            webAppUser.setBio(bio);
        }
        if (profilePictureName != null && !profilePictureName.trim().isEmpty()) {
            if (webAppUser.getProfilePicture() != null) {
                CloudUtils.deleteBlob(CloudUtils.resolveUserProfilePictureLocation(
                        webAppUser.getUserId(),
                        webAppUser.getProfilePicture()
                ));
            }

            webAppUser.setProfilePicture(profilePictureName);
            String fileLocation = CloudUtils.resolveUserProfilePictureLocation(
                    webAppUser.getUserId(),
                    profilePictureName);
            CloudUtils.putStorageKey(profilePicture, fileType, fileLocation);
        }
        webAppUserRepository.save(webAppUser);

        return ResponseEntity.ok("User profile updated successfully");
    }
}