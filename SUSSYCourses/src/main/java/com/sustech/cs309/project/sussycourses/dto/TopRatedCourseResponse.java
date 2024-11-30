package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record TopRatedCourseResponse(
        Long courseId,
        String courseName,
        String description,
        String topic,
        String coverImageUrl,
        Long teacherId,
        String teacherName,
        String teacherEmail,
        String teacherProfilePicture,
        String type,
        Float totalEvaluationScore,
        Integer numEvaluations,
        Float averageRating,
        Float weightedRating,
        LocalDateTime createdAt
) {
}
