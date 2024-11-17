package com.sustech.cs309.project.sussycourses.dto;

public record UpdateUserRequest(
        String fullName,
        String gender,
        String bio,
        String profilePicture
) {
}
