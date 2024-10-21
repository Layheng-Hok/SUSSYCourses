package com.sustech.cs309.project.sussycourses.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    @GetMapping({"/", "/home"})
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/admin/home")
    public String showAdminHomePage() {
        return "home_admin";
    }

    @GetMapping("/student/home")
    public String showStudentHomePage() {
        return "home_student";
    }

    @GetMapping("/instructor/home")
    public String showInstructorHomePage() {
        return "home_instructor";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }
}

