package com.sustech.cs309.project.sussycourses.controller;


import com.sustech.cs309.project.sussycourses.dto.AdminCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.dto.CourseCreationRequest;
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

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/approved")
    public List<AdminCourseDetailResponse> getAllApprovedCourses() {
        /* *
        ! TO DO LATER
        */
        return courseService.getCoursesByStatus("pending");
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PostMapping("/create")
    public ResponseEntity<String> createCourse(@ModelAttribute CourseCreationRequest courseCreationRequest) throws Exception {
        return courseService.createCourse(courseCreationRequest);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PutMapping("/update")
    public ResponseEntity<String> updateCourse(@RequestBody CourseCreationRequest newCourse) throws Exception {
        return courseService.updateCourse(newCourse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/approve/{courseId}")
    public ResponseEntity<String> approveCourse(@PathVariable Long courseId) {
        return courseService.approveCourse(courseId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/reject/{courseId}")
    public ResponseEntity<String> rejectCourse(@PathVariable Long courseId) {
        return courseService.rejectCourse(courseId);
    }
}
