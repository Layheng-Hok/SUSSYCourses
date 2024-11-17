package com.sustech.cs309.project.sussycourses.controller;


import com.sustech.cs309.project.sussycourses.dto.AdminCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public List<AdminCourseDetailResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/pending")
    public List<AdminCourseDetailResponse> getAllPendingCourses() {
        return courseService.getCoursesByStatus("pending");
    }

//    @PostMapping("/course/create")
//    public ResponseEntity<String> createCourse(@RequestBody Course newCourse) {
//        Course course = new Course();
//        courseRepository.save(newCourse);
//        return ResponseEntity.ok("Course Submitted Successfully! Awaiting Admin Approval.");
//    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/approve/{courseId}")
    public ResponseEntity<String> approveCourse(@PathVariable Long courseId) {
        return courseService.approveCourse(courseId);
    }
}
