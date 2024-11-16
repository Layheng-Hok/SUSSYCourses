package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        String fullName,
        String message,
        String reply,
        LocalDateTime createdAt
) {
}
