package com.sustech.cs309.project.sussycourses.controller;


import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    //Student Functions
    @GetMapping("/all")
    public List<Course> all() {
        return courseRepository.findAll();
    }
    //Teacher Functions
    @PostMapping("/create")
    public ResponseEntity<String> createCourse(@RequestBody Course newCourse) {
        courseRepository.save(newCourse);
        return ResponseEntity.ok("Course Submitted Successfully! Awaiting Admin Approval.");
    }

    //Admin Functions
    @PutMapping("/approve")
    public String approveCourse(@RequestBody Long id) {
        return courseService.approveCourse(id);
    }
}
