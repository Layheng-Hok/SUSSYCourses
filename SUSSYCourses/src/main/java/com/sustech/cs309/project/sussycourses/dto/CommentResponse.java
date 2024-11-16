package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        Long replyId,
        String replyMessage,
        String fullName,
        String message,
        String reply,
        LocalDateTime createdAt,
        String repliedTo
) {
}
