package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record StudentCourseDetailResponse(
        Long courseId,
        String courseName,
        String description,
        String topic,
        String coverImageUrl,
        Long teacherId,
        String teacherName,
        String type,
        String enrollmentStatus,
        Boolean liked,
        Float averageRating,
        LocalDateTime createdAt
) {
}
