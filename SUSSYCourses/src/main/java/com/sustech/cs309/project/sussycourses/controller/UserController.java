package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.InstructorDetailResponse;
import com.sustech.cs309.project.sussycourses.dto.StudentDetailResponse;
import com.sustech.cs309.project.sussycourses.dto.UserResponse;
import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import com.sustech.cs309.project.sussycourses.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class UserController {
    private final WebAppUserService webAppUserService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public List<UserResponse> findAll() {
        return webAppUserService.findAllUser();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STUDENT', 'ROLE_INSTRUCTOR')")
    @GetMapping("/users/{email}")
    public UserResponse findByEmail(@PathVariable String email) {
        return webAppUserService.findByEmail(email);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/student/profile/{userId}")
    public StudentDetailResponse getStudentById(@PathVariable long userId) {
        return webAppUserService.getStudentById(userId);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/instructor/profile/{userId}")
    public InstructorDetailResponse getInstructorById(@PathVariable long userId) {
        return webAppUserService.getInstructorById(userId);
    }

    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR', 'ROLE_STUDENT')")
    @PutMapping("/users/update/{userId}")
    public void updateInstructorProfile(@PathVariable long userId, @RequestBody UpdateUserRequest updateUserRequest) {
        webAppUserService.updateInstructorProfile(userId, updateUserRequest);
    }

}
