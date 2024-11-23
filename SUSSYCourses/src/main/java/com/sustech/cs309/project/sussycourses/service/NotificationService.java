package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Notification;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.NotificationCreationRequest;
import com.sustech.cs309.project.sussycourses.repository.CourseStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.NotificationRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final WebAppUserRepository webAppUserRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final NotificationRepository notificationRepository;

    public ResponseEntity<String> notifyStudent(Long teacherId, Long courseId, NotificationCreationRequest notificationCreationRequest) {
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
}
