package com.sustech.cs309.project.sussycourses.service;


import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CourseService  {
    @Autowired
    CourseRepository courseRepository;

    public String approveCourse(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setStatus("APPROVED");
            courseRepository.save(course);
            return "Course approved successfully.";
        } else {
            throw new NoSuchElementException("Course not found.");
        }
    }
}
