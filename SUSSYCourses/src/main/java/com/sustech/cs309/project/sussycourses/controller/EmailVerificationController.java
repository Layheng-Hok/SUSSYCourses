package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmailVerificationController {
    @Autowired
    private WebAppUserService webAppUserService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("verificationToken") String token, Model model) {
        String result = webAppUserService.validateVerificationToken(token);
        if (result.equals("valid")) {
            model.addAttribute("message", "Your account has been verified successfully.");
            return "verified";
        } else {
            model.addAttribute("message", "Invalid verification token.");
            return "verify-email";
        }
    }
}
