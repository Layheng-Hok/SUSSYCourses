package com.sustech.cs309.project.sussycourses.dto;

import java.util.List;

public record NotificationListResponse(
        Integer numNotifications,
        List<NotificationResponse> notifications
) {
}
