package com.sustech.cs309.project.sussycourses.dto;

public record InstructorCourseDetailResponse(
        Long courseId,
        String courseName,
        String description,
        String topic,
        String coverImageUrl,
        String type,
        String approvalStatus
) {
}
