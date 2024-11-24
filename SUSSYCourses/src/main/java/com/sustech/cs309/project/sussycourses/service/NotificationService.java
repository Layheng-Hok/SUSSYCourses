package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
import com.sustech.cs309.project.sussycourses.domain.Notification;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.NotificationCreationRequest;
import com.sustech.cs309.project.sussycourses.dto.NotificationListResponse;
import com.sustech.cs309.project.sussycourses.dto.NotificationResponse;
import com.sustech.cs309.project.sussycourses.repository.CourseStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.NotificationRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final WebAppUserRepository webAppUserRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final NotificationRepository notificationRepository;

    public NotificationListResponse getUserMailboxPaginated(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByReceiver_UserId(userId, pageable);

        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(notification -> new NotificationResponse(
                        notification.getSender().getFullName(),
                        notification.getSender().getEmail(),
                        notification.getReceiver().getFullName(),
                        notification.getReceiver().getEmail(),
                        notification.getSubject(),
                        notification.getText(),
                        notification.getCreatedAt()
                ))
                .toList();

        Long totalNotifications = notificationRepository.countByReceiver_UserId(userId);

        return new NotificationListResponse(totalNotifications, notificationResponses);
    }

    public NotificationListResponse getUserSentMailsPaginated(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findBySender_UserId(userId, pageable);

        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(notification -> new NotificationResponse(
                        notification.getSender().getFullName(),
                        notification.getSender().getEmail(),
                        notification.getReceiver().getFullName(),
                        notification.getReceiver().getEmail(),
                        notification.getSubject(),
                        notification.getText(),
                        notification.getCreatedAt()
                ))
                .toList();

        Long totalNotifications = notificationRepository.countBySender_UserId(userId);

        return new NotificationListResponse(totalNotifications, notificationResponses);
    }

    public ResponseEntity<String> getUserMailboxPaginated(Long teacherId, Long courseId, NotificationCreationRequest notificationCreationRequest) {
        Optional<WebAppUser> teacherOptional = webAppUserRepository.findByUserIdAndRoleRoleId(
                teacherId, 3);
        if (teacherOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Instructor not found");
        }

        Optional<WebAppUser> studentOptional = webAppUserRepository.findByEmailAndRoleRoleId(
                notificationCreationRequest.receiverEmail(), 2);
        if (studentOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Student not found");
        }

        WebAppUser teacher = teacherOptional.get();
        WebAppUser student = studentOptional.get();
        if (courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseIdAndStatus(student.getUserId(), courseId, "enrolled").isEmpty()) {
            return ResponseEntity.status(404).body("Student not enrolled in this course");
        }

        Notification notification = new Notification();
        notification.setSender(teacher);
        notification.setReceiver(student);
        notification.setSubject(notificationCreationRequest.subject());
        notification.setText(notificationCreationRequest.text());
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        return ResponseEntity.ok().body("Notification sent successfully");
    }

    public ResponseEntity<String> notifyAllStudents(Long teacherId, Long courseId, NotificationCreationRequest notificationCreationRequest) {
        List<CourseStudent> courseStudents = courseStudentRepository.findByCourse_CourseId(courseId);
        for (CourseStudent courseStudent : courseStudents) {
            NotificationCreationRequest newNotificationCreationRequest = new NotificationCreationRequest(
                    courseStudent.getStudent().getEmail(),
                    notificationCreationRequest.subject(),
                    notificationCreationRequest.text()
            );

            ResponseEntity<String> response = getUserMailboxPaginated(teacherId, courseId, newNotificationCreationRequest);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to notify student: " + courseStudent.getStudent().getEmail());
            }
        }
        return ResponseEntity.ok().body("Notifications sent successfully");
    }
}
