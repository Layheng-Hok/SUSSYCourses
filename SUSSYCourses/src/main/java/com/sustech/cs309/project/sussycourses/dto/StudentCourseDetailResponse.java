package com.sustech.cs309.project.sussycourses.dto;

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
        boolean liked
) {
}
