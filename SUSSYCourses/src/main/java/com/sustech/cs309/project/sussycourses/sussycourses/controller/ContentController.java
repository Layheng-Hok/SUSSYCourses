package com.sustech.cs309.project.sussycourses.sussycourses.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    @GetMapping({"/", "/home"})
    public String handleWelcome() {
        return "home";
    }

    @GetMapping("/admin/home")
    public String handleAdminHome() {
        return "home_admin";
    }

    @GetMapping("/student/home")
    public String handleStudentHome() {
        return "home_student";
    }

    @GetMapping("/instructor/home")
    public String handleInstructorHome() {
        return "home_instructor";
    }

    @GetMapping("/login")
    public String handleLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String handleRegister() {
        return "register";
    }
}

