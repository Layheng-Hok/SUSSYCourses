package com.sustech.cs309.project.sussycourses.dto;

public record CommentRequest (
        Long courseId,
        Long userId,
        String message){}
