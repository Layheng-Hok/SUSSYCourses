package com.sustech.cs309.project.sussycourses.dto;

import org.springframework.web.multipart.MultipartFile;

public record CourseCreationRequest(
        String courseName,
        String description,
        Long teacherId,
        String type,
        String topic,
        String coverImageName,
        String fileType,
        MultipartFile file
) {
}
