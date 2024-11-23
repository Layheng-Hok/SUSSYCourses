package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.NotificationCreationRequest;
import com.sustech.cs309.project.sussycourses.dto.NotificationListResponse;
import com.sustech.cs309.project.sussycourses.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STUDENT','ROLE_INSTRUCTOR')")
    @GetMapping("users/{userId}/mailbox")
    public NotificationListResponse getUserMailbox(@PathVariable Long userId) {
        return notificationService.getUserMailbox(userId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_STUDENT','ROLE_INSTRUCTOR')")
    @GetMapping("users/{userId}/sent")
    public NotificationListResponse getUserSentMails(@PathVariable Long userId) {
        return notificationService.getUserSentMails(userId);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @Transactional
    @PostMapping("instructors/{userId}/courses/{courseId}/notify")
    public ResponseEntity<String> notifyStudent(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @RequestBody NotificationCreationRequest notificationCreationRequest) {
        return notificationService.getUserMailbox(userId, courseId, notificationCreationRequest);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @Transactional
    @PostMapping("instructors/{userId}/courses/{courseId}/notify-all")
    public ResponseEntity<String> notifyAllStudents(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @RequestBody NotificationCreationRequest notificationCreationRequest) {
        return notificationService.notifyAllStudents(userId, courseId, notificationCreationRequest);
    }
}
