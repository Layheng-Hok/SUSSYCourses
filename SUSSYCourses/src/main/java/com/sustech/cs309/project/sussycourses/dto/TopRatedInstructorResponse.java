package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;
import java.util.List;

public record TopRatedInstructorResponse(
        Long userId,
        String fullName,
        String email,
        String profileImageUrl,
        String gender,
        String roleName,
        String bio,
        Float averageRating,
        Float weightedRating,
        Long totalStudents,
        Integer numCourses,
        List<BasicCourseResponse> courses,
        LocalDateTime createdAt
) {
}
