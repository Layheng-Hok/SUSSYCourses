package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.domain.Role;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.UserResponse;
import com.sustech.cs309.project.sussycourses.service.WebAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class UserController {
    private final WebAppUserService webAppUserService;

    private final List<WebAppUser> users = new ArrayList<>(List.of(
        createUser(1L, "John Doe", "john.doe@example.com", "ROLE_USER"),
        createUser(2L, "Jane Smith", "jane.smith@example.com", "ROLE_ADMIN"),
        createUser(3L, "Sam Wilson", "sam.wilson@example.com", "ROLE_USER")
    ));

    @GetMapping("/{userId}")
    public WebAppUser getUserById(@PathVariable Long userId) {
        return users.stream()
            .filter(user -> user.getUserId().equals(userId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    

     // Helper method to create WebAppUser instances
     private static WebAppUser createUser(Long id, String fullName, String email, String roleName) {
        WebAppUser user = new WebAppUser();
        user.setUserId(id);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setEnabled(true);

        Role role = new Role();
        role.setRoleId(1); 
        role.setRoleName(roleName);
        user.setRole(role);

        return user;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public List<UserResponse> findAll() {
        return webAppUserService.findAllUser();
    }
}
