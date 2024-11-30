package com.sustech.cs309.project.sussycourses.controller;


import com.sustech.cs309.project.sussycourses.dto.*;
import com.sustech.cs309.project.sussycourses.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/approved")
    public ApprovedCoursesResponse getApprovedCoursesPaginated(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return courseService.getApprovedCoursesPaginated(page, size);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/pending")
    public List<AdminCourseDetailResponse> getAllPendingCourses() {
        return courseService.getAllPendingCourses();
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
    @PutMapping("/{courseId}/approve")
    public ResponseEntity<String> approveCourse(@PathVariable Long courseId) {
        return courseService.approveCourse(courseId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{courseId}/reject")
    public ResponseEntity<String> rejectCourse(@PathVariable Long courseId) {
        return courseService.rejectCourse(courseId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_INSTRUCTOR', 'ROLE_PUBLIC')")
    @GetMapping("/instructors/{instructorId}")
    public List<BasicCourseResponse> getCoursesByInstructorId(@PathVariable Long instructorId) {
        return courseService.getCoursesByInstructorId(instructorId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_INSTRUCTOR', 'ROLE_PUBLIC')")
    @GetMapping("/top-rated")
    public List<TopRatedCourseResponse> getTopRatedCourses() throws IOException {
        return courseService.getTopRatedCourses();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STUDENT', 'ROLE_INSTRUCTOR', 'ROLE_PUBLIC')")
    @GetMapping("/instructors/top-rated")
    public List<TopRatedInstructorResponse> getTopRatedInstructors() throws IOException {
        return courseService.getTopRatedInstructors();
    }
}
