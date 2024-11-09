package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final WebAppUserRepository webAppUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        WebAppUser webAppUser = webAppUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setWebAppUser(webAppUser);

        return customUserDetails;
    }
}
