package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.domain.Courseware;
import com.sustech.cs309.project.sussycourses.dto.CoursewareListResponse;
import com.sustech.cs309.project.sussycourses.dto.CoursewareRequest;
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
@RequestMapping("/courseware")
public class CoursewareController {
    private final CoursewareService coursewareService;

    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_INSTRUCTOR')")
    @GetMapping("/coursewarePage")
    public String coursePage() throws IOException {
        return coursewareService.retrieveCoursewareData();
    }

    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @GetMapping("/{variantOf}/allVersions")
        public List<CoursewareVersionResponse> coursewareVersions(@PathVariable long variantOf) throws IOException {
            return coursewareService.retrieveCoursewareVersions(variantOf);
        }
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PostMapping("/create")
    public ResponseEntity<String> createCourseware(CoursewareRequest coursewareRequest) throws Exception {
        return coursewareService.uploadCourseware(coursewareRequest);
    }
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    @PutMapping("/update")
    public ResponseEntity<String> updateCourseware(@RequestBody UpdateCoursewareRequest updateCoursewareRequest) throws Exception {
        return coursewareService.updateCourseware(updateCoursewareRequest);
    }
}
