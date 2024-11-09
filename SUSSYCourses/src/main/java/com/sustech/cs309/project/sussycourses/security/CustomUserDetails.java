package com.sustech.cs309.project.sussycourses.security;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
public class CustomUserDetails implements UserDetails {
    private WebAppUser webAppUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + webAppUser.getRole().getRoleName());
    }

    @Override
    public String getPassword() {
        return webAppUser.getPassword();
    }

    @Override
    public String getUsername() {
        return webAppUser.getEmail();
    }
}
