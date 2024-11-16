package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long userId,
        String fullName,
        String email,
        String profileImageUrl,
        String gender,
        String roleName,
        String bio,
        LocalDateTime createdAt
) {
}
