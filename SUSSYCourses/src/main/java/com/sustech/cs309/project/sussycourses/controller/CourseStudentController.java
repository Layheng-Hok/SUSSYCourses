package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.StudentCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.dto.StudentCourseListResponse;
import com.sustech.cs309.project.sussycourses.service.CourseStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CourseStudentController {
    private final CourseStudentService courseStudentService;

    @PreAuthorize("hasRole('ROLE_STUDENTS')")
    @PutMapping("/students/{userId}/courses/{courseId}/join")
    public ResponseEntity<String> joinOpenCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        return courseStudentService.joinOpenCourse(userId, courseId);
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
    @PutMapping("/students/{userId}/courses/{courseId}/like-unlike")
    public ResponseEntity<String> likeOrUnlikeCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        return courseStudentService.likeOrUnlikeCourse(userId, courseId);
    }
}
