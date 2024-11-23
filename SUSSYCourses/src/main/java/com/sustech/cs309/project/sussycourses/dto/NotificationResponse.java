package com.sustech.cs309.project.sussycourses.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        String senderFullName,
        String senderEmail,
        String receiverFullName,
        String receiverEmail,
        String subject,
        String text,
        LocalDateTime createdAt
) {
}
