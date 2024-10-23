package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService {

    @Autowired
    private WebAppUserRepository userRepository;

    public void createVerificationToken(WebAppUser webAppUser, String token) {
        webAppUser.setVerificationToken(token);
        userRepository.save(webAppUser);
    }

    public String validateVerificationToken(String token) {
        WebAppUser webAppUser = userRepository.findByVerificationToken(token).orElse(null);
        if (webAppUser == null) {
            return "invalid";
        }

        webAppUser.setEnabled(true);
        userRepository.save(webAppUser);
        return "valid";
    }
}
