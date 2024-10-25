package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.RegistrationDto;
import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam("verificationToken") String verificationToken) {
        String result = webAppUserService.validateVerificationToken(verificationToken);
        Map<String, String> response = new HashMap<>();
        if ("valid".equals(result)) {
            response.put("message", "Your account has been verified successfully, please return to the site for login");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Link has expired");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginWebAppUser(@RequestBody LoginDto loginDto) {
//        return webAppUserService.loginWebAppUser(loginDto);
//    }
}
