package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.*;
import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public StudentDetailResponse getStudentUserDetail(@PathVariable Long userId) {
        return webAppUserService.getStudentUserDetail(userId);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/students/{userId}/courses")
    public StudentCourseListResponse getAllCoursesByStudentId(@PathVariable Long userId) {
        return webAppUserService.getAllCoursesByStudentId(userId);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/students/{userId}/courses/{courseId}")
    public StudentCourseDetailResponse getCourseDetailForStudent(
            @PathVariable Long userId,
            @PathVariable Long courseId) {
        return webAppUserService.getCourseDetailForStudent(userId, courseId);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/instructor/profile/{userId}")
    public InstructorDetailResponse getInstructorById(@PathVariable long userId) {
        return webAppUserService.getInstructorById(userId);
    }

    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR', 'ROLE_STUDENT')")
    @PutMapping("/users/update/{userId}")
    public void updateInstructorProfile(@PathVariable long userId, @RequestBody UpdateUserRequest updateUserRequest) throws Exception {
        webAppUserService.updateInstructorProfile(userId, updateUserRequest);
    }

}
