package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.*;
import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WebAppUserController {
    private final WebAppUserService webAppUserService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public List<UserResponse> findAll() {
        return webAppUserService.findAllUsers();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STUDENT', 'ROLE_INSTRUCTOR')")
    @GetMapping("/users/{email}")
    public UserResponse findByEmail(@PathVariable String email) {
        return webAppUserService.findByEmail(email);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/students/{userId}")
    public StudentDetailResponse getStudentUserDetail(@PathVariable Long userId) throws IOException {
        return webAppUserService.getStudentUserDetail(userId);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/instructors/{userId}")
    public InstructorDetailResponse getInstructorById(@PathVariable Long userId) throws IOException {
        return webAppUserService.getInstructorById(userId);
    }

    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR')")
    @PutMapping("/users/update/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long userId, @ModelAttribute UpdateUserRequest updateUserRequest) throws Exception {
        return webAppUserService.updateUserProfile(userId, updateUserRequest);
    }

    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR')")
    @PutMapping("/users/{userId}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordRequest changePasswordRequest) {
        return webAppUserService.updatePassword(userId, changePasswordRequest);
    }
}
