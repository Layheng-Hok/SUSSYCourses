package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        String commentFullName,
        String commentProfilePictureUrl,
        String commentMessage,
        String commentAttachment,
        LocalDateTime commentCreatedAt,
        Long replyToId,
        String replyToFullName,
        String replyToProfilePictureUrl,
        String replyToMessage,
        String replyToAttachment,
        LocalDateTime replyToCreatedAt
) {
}
