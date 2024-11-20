package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.CoursewareRequest;
import com.sustech.cs309.project.sussycourses.service.CoursewareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
