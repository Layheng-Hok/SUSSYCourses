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


    @GetMapping("/coursePage")
    public String coursePage() throws IOException {
        List<JSONObject> data = new ArrayList<>();
        List<Course> courses = courseRepository.findCoursesWithTeacherInfo();

        for (Course course : courses) {
            JSONObject courseData = new JSONObject();
            List<Courseware> courseware = coursewareRepository.findCoursewareForCourse(course.getCourseId());

            // Basic course information
            courseData.put("id", course.getCourseId());
            courseData.put("name", course.getCourseName());
            courseData.put("description", course.getDescription());
            courseData.put("image", cloudController.getStorageKey(course.getCoverImage())); // Adjust as per your image source

            // Instructor info
            if (course.getTeacher() != null) {
                courseData.put("instructorName", course.getTeacher().getFullName());
                courseData.put("instructorImage", "/assets/Avatars/instructor.jpg"); // Adjust as per your image source
                courseData.put("instructorBio", course.getTeacher().getBio());
            }

            // Teaching chapters (mock data, adjust as per your real structure)
            JSONArray teachingChapters = new JSONArray();

            JSONArray homeworkChapters = new JSONArray();
            JSONArray projectChapters = new JSONArray();
            for (Courseware c : courseware) {
                if (Objects.equals(c.getCategory(), "lecture")) {
                    if (teachingChapters.length() == c.getChapter()) {
                        JSONObject chapter = teachingChapters.getJSONObject(c.getChapter() - 1); // Retrieve the chapter as a JSONObject
                        JSONArray materials = chapter.getJSONArray("materials");            // Get the "materials" array

                        JSONObject material = new JSONObject();
                        String getMaterial = cloudController.getStorageKey(c.getUrl());
                        material.put("title", getMaterial);
                        materials.put(material);
                    } else if (teachingChapters.length() < c.getChapter()) {
                        JSONObject teachingChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = cloudController.getStorageKey(c.getUrl());
                        material.put("title", getMaterial);
                        teachingChapter.put("name", "Chapter " + c.getChapter());
                        materials.put(material);
                        teachingChapter.put("materials", materials);
                        teachingChapters.put(teachingChapter);
                    }
                } else if (Objects.equals(c.getCategory(), "assignment")) {
                    if (teachingChapters.length() == c.getChapter()) {
                        JSONObject chapter = homeworkChapters.getJSONObject(c.getChapter() - 1); // Retrieve the chapter as a JSONObject
                        JSONArray materials = chapter.getJSONArray("materials");            // Get the "materials" array

                        JSONObject material = new JSONObject();
                        String getMaterial = cloudController.getStorageKey(c.getUrl());
                        material.put("title", getMaterial);
                        materials.put(material);
                    } else if (homeworkChapters.length() < c.getChapter()) {
                        JSONObject homeworkChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = cloudController.getStorageKey(c.getUrl());
                        material.put("title", getMaterial);
                        materials.put(material);
                        homeworkChapter.put("name", "Homework " + c.getChapter());
                        homeworkChapter.put("materials", materials);
                    }
                } else if (Objects.equals(c.getCategory(), "project")) {
                    if (projectChapters.length() == c.getChapter()) {
                        JSONObject chapter = projectChapters.getJSONObject(c.getChapter() - 1); // Retrieve the chapter as a JSONObject
                        JSONArray materials = chapter.getJSONArray("materials");            // Get the "materials" array

                        JSONObject material = new JSONObject();
                        String getMaterial = cloudController.getStorageKey(c.getUrl());
                        material.put("title", getMaterial);
                        materials.put(material);
                    } else if (projectChapters.length() < c.getChapter()) {
                        JSONObject projectChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = cloudController.getStorageKey(c.getUrl());
                        material.put("title", getMaterial);
                        materials.put(material);
                        projectChapter.put("name", "Project " + c.getChapter());
                        projectChapter.put("materials", materials);

                    }
                }
            }
            courseData.put("teachingChapters", teachingChapters);
            courseData.put("homeworkChapters", homeworkChapters);
            courseData.put("projectChapters", projectChapters);
            data.add(courseData);
        }

        return data.toString();
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
