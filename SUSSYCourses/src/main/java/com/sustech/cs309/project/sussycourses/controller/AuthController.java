package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.RegistrationDto;
import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class AuthController {
    @Autowired
    private WebAppUserService webAppUserService;

    @PostMapping("/register/student")
    public ResponseEntity<String> createStudentUser(@RequestBody RegistrationDto registrationDto) {
        return webAppUserService.createWebAppUser(registrationDto);
    }

    @PostMapping("/register/instructor")
    public ResponseEntity<String> createInstructorUser(@RequestBody RegistrationDto registrationDto) {
        return webAppUserService.createWebAppUser(registrationDto);
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginWebAppUser(@RequestBody LoginDto loginDto) {
//        return webAppUserService.loginWebAppUser(loginDto);
//    }
}
