package com.sustech.cs309.project.sussycourses.dto;

public record WebAppUserResponse(
        Long userId,
        String fullName,
        String email,
        String profileImageUrl,
        String gender,
        String roleName,
        Integer points,
        String bio,
        Integer numCoursesEnrolled
) {
}
