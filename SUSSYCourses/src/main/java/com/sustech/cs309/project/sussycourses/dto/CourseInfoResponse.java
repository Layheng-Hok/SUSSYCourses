package com.sustech.cs309.project.sussycourses.dto;

public record CourseInfoResponse(
        Long courseId,
        String courseName,
        String description,
        String coverImageUrl,
        Long teacherId,
        String teacherName,
        String type,
        String enrollmentStatus,
        boolean liked
) {
}
