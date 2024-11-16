package com.sustech.cs309.project.sussycourses.controller;


import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.Courseware;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.CoursewareRepository;
import com.sustech.cs309.project.sussycourses.service.CourseService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CoursewareRepository coursewareRepository;
    @Autowired
    private CloudController cloudController;

    //Student Functions
    @GetMapping("/all")
    public List<Course> all() {
        return courseRepository.findAll();
    }

    //Teacher Functions
    @PostMapping("/create")
    public ResponseEntity<String> createCourse(@RequestBody Course newCourse) {
        Course course = new Course();
        courseRepository.save(newCourse);
        return ResponseEntity.ok("Course Submitted Successfully! Awaiting Admin Approval.");
    }

    //Admin Functions
    @PutMapping("/approve")
    public String approveCourse(@RequestBody Long id) {
        return courseService.approveCourse(id);
    }
}
