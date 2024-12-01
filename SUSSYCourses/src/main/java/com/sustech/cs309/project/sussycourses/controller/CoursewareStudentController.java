package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.CourseProgressResponse;
import com.sustech.cs309.project.sussycourses.service.CoursewareStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CoursewareStudentController {
    private final CoursewareStudentService coursewareStudentService;

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/students/{studentId}/courses/{courseId}/progress")
    public CourseProgressResponse getCourseProgressByStudentIdAndCourseId(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return coursewareStudentService.getCourseProgressByStudentIdAndCourseId(studentId, courseId);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PutMapping("/students/{studentId}/coursewares/{coursewareId}/completed")
    public ResponseEntity<String> setCoursewareCompletedForStudent(
            @PathVariable Long studentId,
            @PathVariable Long coursewareId) {
        return coursewareStudentService.setCoursewareCompletedForStudent(studentId, coursewareId);
    }
}
