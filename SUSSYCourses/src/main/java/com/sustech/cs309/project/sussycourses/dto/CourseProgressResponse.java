package com.sustech.cs309.project.sussycourses.dto;

public record CourseProgressResponse(
        Integer totalTeachingMaterials,
        Integer completedTeachingMaterials,
        Integer totalCourseworkMaterials,
        Integer completedCourseworkMaterials
) {
}
