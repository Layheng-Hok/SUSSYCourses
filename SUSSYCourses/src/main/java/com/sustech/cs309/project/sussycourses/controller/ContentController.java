package com.sustech.cs309.project.sussycourses.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentController {
//    @GetMapping({"/", "/home"})
//    public String showHomePage() {
//        return "home";
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/home")
    public ResponseEntity<String> showAdminHomePage() {
        String response = "admin";
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/student/home")
    public ResponseEntity<String> showStudentHomePage() {
        String response = "student";
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/instructor/home")
    public ResponseEntity<String> showInstructorHomePage() {
        String response = "instructor";
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String showRegisterPage() {
//        return "register";
//    }
}

