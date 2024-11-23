package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.NotificationCreationRequest;
import com.sustech.cs309.project.sussycourses.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @Transactional
    @PostMapping("instructors/{userId}/courses/{courseId}/notify")
    public ResponseEntity<String> notifyStudent(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @RequestBody NotificationCreationRequest notificationCreationRequest) {
        return notificationService.notifyStudent(userId, courseId, notificationCreationRequest);
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
