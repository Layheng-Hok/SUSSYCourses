package com.sustech.cs309.project.sussycourses.dto;

import org.springframework.web.multipart.MultipartFile;

public record CoursewareRequest(
    Long courseId,
    String fileType,
    String category,
    Boolean downloadable,
    Integer chapter,
    Integer order,
    MultipartFile file) {}