//package com.sustech.cs309.project.sussycourses.service;
//
//import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
//import com.sustech.cs309.project.sussycourses.exception.UserNotVerifiedException;
//import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class WebAppUserDetailsService implements UserDetailsService {
//    @Autowired
//    private WebAppUserRepository webAppUserRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<WebAppUser> webAppUser = webAppUserRepository.findByEmail(email);
//
//        if (webAppUser.isEmpty()) {
//            throw new UsernameNotFoundException(email);
//        }
//
//        WebAppUser webAppUserObj = webAppUser.get();
//
//        if (!webAppUserObj.isEnabled()) {
//            throw new UserNotVerifiedException("User not verified: Please check your email for verification");
//        }
//
//        return User.builder()
//                .username(webAppUserObj.getEmail())
//                .password(webAppUserObj.getPassword())
//                .roles(String.valueOf(webAppUserObj.getRole()))
//                .build();
//    }
//}
