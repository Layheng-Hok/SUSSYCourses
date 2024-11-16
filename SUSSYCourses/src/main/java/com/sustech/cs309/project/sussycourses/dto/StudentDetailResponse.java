package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;
import java.util.List;

public record StudentDetailResponse(
        Long userId,
        String fullName,
        String email,
        String profileImageUrl,
        String gender,
        String roleName,
        Integer points,
        String bio,
        LocalDateTime createdAt,
        Integer numCoursesEnrolled,
        List<CourseInfoResponse> coursesEnrolled
) {
}
