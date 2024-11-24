package com.sustech.cs309.project.sussycourses.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateCoursewareRequest(
        Long coursewareId,
        Long courseId,
        String fileType,
        String category,
        Boolean downloadable,
        Integer chapter,
        Integer order,
        Long variant_of,
        Integer version,
        MultipartFile file) {}