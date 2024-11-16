package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;
import java.util.List;

public record InstructorDetailResponse(
        Long userId,
        String fullName,
        String email,
        String profileImageUrl,
        String gender,
        String roleName,
        String bio,
        LocalDateTime createdAt,
        Integer numCourses,
        List<InstructorCourseDetailResponse> courses
) {
}
