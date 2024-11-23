package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.StudentCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.dto.StudentCourseListResponse;
import com.sustech.cs309.project.sussycourses.service.CourseStudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CourseStudentController {
    private final CourseStudentService courseStudentService;

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Transactional
    @PostMapping("/students/{userId}/courses/{courseId}/enroll")
    public ResponseEntity<String> joinCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        return courseStudentService.joinCourse(userId, courseId);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/students/{userId}/courses")
    public StudentCourseListResponse getAllCoursesByStudentId(@PathVariable Long userId) {
        return courseStudentService.getAllCoursesByStudentId(userId);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/students/{userId}/courses/{courseId}")
    public StudentCourseDetailResponse getCourseDetailForStudent(
            @PathVariable Long userId,
            @PathVariable Long courseId) throws IOException {
        return courseStudentService.getCourseDetailForStudent(userId, courseId);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Transactional
    @PutMapping("/students/{userId}/courses/{courseId}/like-unlike")
    public ResponseEntity<String> likeOrUnlikeCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        return courseStudentService.likeOrUnlikeCourse(userId, courseId);
    }
}
