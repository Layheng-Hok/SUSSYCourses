package com.sustech.cs309.project.sussycourses.controller;


import com.sustech.cs309.project.sussycourses.dto.AdminCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/courses")
    public List<AdminCourseDetailResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/courses/pending")
    public List<AdminCourseDetailResponse> getAllPendingCourses() {
        return courseService.getCoursesByStatus("pending");
    }

//    @PostMapping("/course/create")
//    public ResponseEntity<String> createCourse(@RequestBody Course newCourse) {
//        Course course = new Course();
//        courseRepository.save(newCourse);
//        return ResponseEntity.ok("Course Submitted Successfully! Awaiting Admin Approval.");
//    }

    @PutMapping("/course/approve")
    public String approveCourse(@RequestBody Long id) {
        return courseService.approveCourse(id);
    }
}
