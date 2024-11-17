package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.service.CoursewareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/courseware")
public class CoursewareController {

    @Autowired
    CoursewareService coursewareService;

    @GetMapping("/coursewarePage")
    public String coursePage() throws IOException {
        return coursewareService.retrieveCoursewareData();
    }

//    @PostMapping("/create")
//    public String createCourseware() throws IOException {
//        return coursewareService.uploadCourseware();
//    }
}
