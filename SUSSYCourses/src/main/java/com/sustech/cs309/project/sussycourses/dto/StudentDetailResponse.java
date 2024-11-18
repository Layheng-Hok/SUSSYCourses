package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record StudentDetailResponse(
        Long userId,
        String fullName,
        String email,
        String profileImageUrl,
        String gender,
        String roleName,
        Integer points,
        String bio,
        LocalDateTime createdAt
) {
}
