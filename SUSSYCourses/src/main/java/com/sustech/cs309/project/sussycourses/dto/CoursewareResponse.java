package com.sustech.cs309.project.sussycourses.dto;

public record CoursewareResponse(
        Long courseId,
        String courseType,
        Long coursewareId,
        String fileName,
        String fileType,
        String category,
        Boolean downloadable,
        Integer chapter,
        Integer order,
        Long variant_of,
        Integer version,
        String url,
        Boolean isCompleted
) {
}
