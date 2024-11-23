package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record BasicCourseResponse(
        Long courseId,
        String courseName,
        String description,
        String topic,
        String coverImageUrl,
        Long teacherId,
        String teacherName,
        String teacherEmail,
        String type,
        Long likesCount,
        Float averageRating,
        LocalDateTime createdAt
) {
}
