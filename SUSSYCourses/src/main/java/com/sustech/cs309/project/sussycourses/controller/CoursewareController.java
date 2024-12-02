package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.CoursewareRequest;
import com.sustech.cs309.project.sussycourses.dto.CoursewareResponse;
import com.sustech.cs309.project.sussycourses.dto.CoursewareVersionResponse;
import com.sustech.cs309.project.sussycourses.dto.UpdateCoursewareRequest;
import com.sustech.cs309.project.sussycourses.service.CoursewareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CoursewareController {
    private final CoursewareService coursewareService;

    @PreAuthorize("hasAnyRole('ROLE_PUBLIC')")
    @GetMapping("/courses/{courseId}/coursewares")
    public List<CoursewareResponse> getDisplayedCoursewaresByUserIdAndCourseIdForPublicUser(@PathVariable Long courseId) {
        return coursewareService.getDisplayedCoursewaresByUserIdAndCourseIdForPublicUser(courseId);
    }

    @PreAuthorize("hasAnyRole('ROLE_STUDENT')")
    @GetMapping("/students/{studentId}/courses/{courseId}/coursewares")
    public List<CoursewareResponse> getDisplayedCoursewaresByUserIdAndCourseIdForStudent(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return coursewareService.getDisplayedCoursewaresByUserIdAndCourseIdForStudent(studentId, courseId);
    }

    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR')")
    @GetMapping("/users/{userId}/courses/{courseId}/coursewares")
    public String getDisplayedCoursewaresByUserIdAndCourseIdForInstructor(
            @PathVariable Long userId,
            @PathVariable Long courseId) throws IOException {
        return coursewareService.getDisplayedCoursewaresByUserIdAndCourseIdForInstructor(userId, courseId);
    }

    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_INSTRUCTOR')")
    @GetMapping("/courseware/coursewarePage/{courseId}")
    public String coursePage(@PathVariable Long courseId) throws IOException {
        return coursewareService.retrieveCoursewareData(courseId);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/courseware/{variantOf}/allVersions")
    public List<CoursewareVersionResponse> coursewareVersions(@PathVariable long variantOf) throws IOException {
        return coursewareService.retrieveCoursewareVersions(variantOf);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PostMapping("/courseware/create")
    public ResponseEntity<String> createCourseware(@ModelAttribute CoursewareRequest coursewareRequest) throws Exception {
        return coursewareService.createCourseware(coursewareRequest);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PutMapping("/courseware/update")
    public ResponseEntity<String> updateCourseware(@ModelAttribute UpdateCoursewareRequest updateCoursewareRequest) throws Exception {
        return coursewareService.updateCourseware(updateCoursewareRequest);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/courseware/{coursewareId}")
    public CoursewareResponse findByCoursewareId(@PathVariable long coursewareId) throws Exception {
        return coursewareService.findByCoursewareId(coursewareId);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PutMapping("/courseware/{coursewareId}/setActive")
    public ResponseEntity<String> setActiveVersion(@PathVariable Long coursewareId) {
        return coursewareService.setActive(coursewareId);
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PutMapping("/courseware/fixDisplay/{value}")
    public ResponseEntity<String> fixDisplay(@PathVariable Long value) {
        return coursewareService.fixDisplay(value);
    }


    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @DeleteMapping("/courseware/delete/{coursewareId}")
    public ResponseEntity<String> deleteCourseware(@PathVariable Long coursewareId) {
        return coursewareService.deleteCourseware(coursewareId);
    }
}
