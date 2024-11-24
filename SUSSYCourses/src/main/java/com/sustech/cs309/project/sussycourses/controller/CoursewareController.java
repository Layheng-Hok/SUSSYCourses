package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.CoursewareRequest;
import com.sustech.cs309.project.sussycourses.dto.UpdateCoursewareRequest;
import com.sustech.cs309.project.sussycourses.service.CoursewareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courseware")
public class CoursewareController {
    private final CoursewareService coursewareService;

    @GetMapping("/coursewarePage")
    public String coursePage() throws IOException {
        return coursewareService.retrieveCoursewareData();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCourseware(CoursewareRequest coursewareRequest) throws Exception {
        return coursewareService.uploadCourseware(coursewareRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCourseware(UpdateCoursewareRequest updateCoursewareRequest) throws Exception {
        return coursewareService.updateCourseware(updateCoursewareRequest);
    }
}
