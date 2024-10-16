package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebAppUserDetailsService implements UserDetailsService {
    @Autowired
    private WebAppUserRepository webAppUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<WebAppUser> webAppUser = webAppUserRepository.findByUsername(username);

        if (webAppUser.isPresent()) {
            WebAppUser webAppUserObj = webAppUser.get();
            return User.builder()
                    .username(webAppUserObj.getUsername())
                    .password(webAppUserObj.getPassword())
                    .roles(new String[]{webAppUserObj.getRole()})
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
