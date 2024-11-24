package com.sustech.cs309.project.sussycourses.dto;

import java.util.List;

public record NotificationListResponse(
        Long totalNotifications,
        List<NotificationResponse> notifications
) {
}
