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
        String teacherBio,
        String teacherProfilePictureUrl,
        String type,
        String enrollmentStatus,
        Boolean liked,
        Long likesCount,
        Float averageRating,
        LocalDateTime createdAt
) {
}
