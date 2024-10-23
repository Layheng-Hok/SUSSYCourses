package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.RegistrationDto;
import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    private WebAppUserService webAppUserService;


    @PostMapping("/register")
    public ResponseEntity<String> createWebAppUser(@RequestBody RegistrationDto registrationDto) {
        return webAppUserService.createWebAppUser(registrationDto);
    }
}
