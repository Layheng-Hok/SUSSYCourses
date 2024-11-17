package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record AdminCourseDetailResponse(
        Long courseId,
        String courseName,
        String description,
        String topic,
        String coverImageUrl,
        Long teacherId,
        String teacherName,
        String type,
        String status,
        LocalDateTime createdAt
) {
}
