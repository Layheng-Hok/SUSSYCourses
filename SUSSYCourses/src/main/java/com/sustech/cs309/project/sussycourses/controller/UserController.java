package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.StudentDetailResponse;
import com.sustech.cs309.project.sussycourses.dto.UserResponse;
import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class UserController {
    private final WebAppUserService webAppUserService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public List<UserResponse> findAll() {
        return webAppUserService.findAllUser();
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/student/profile/{userId}")
    public StudentDetailResponse getStudentById(@PathVariable long userId) {
        return webAppUserService.getStudentById(userId);
    }
}
