package com.sustech.cs309.project.sussycourses.dto;

/**
 * @param receiverEmail
 * @param subject:      max 100 chars
 * @param text:         max 500 chars
 */
public record NotificationCreationRequest(
        String receiverEmail,
        String subject,
        String text
) {
}
