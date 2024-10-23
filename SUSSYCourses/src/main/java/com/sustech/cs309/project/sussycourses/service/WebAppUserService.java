package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebAppUserService {
    @Autowired
    private WebAppUserRepository webAppUserRepository;

    public String validateVerificationToken(String verificationToken) {
        WebAppUser webAppUser = webAppUserRepository.findByVerificationToken(verificationToken).orElse(null);
        if (webAppUser == null) {
            return "invalid";
        }

        webAppUser.setEnabled(true);
        webAppUserRepository.save(webAppUser);
        return "valid";
    }
}