package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationTokenService {
    private final WebAppUserRepository userRepository;

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
