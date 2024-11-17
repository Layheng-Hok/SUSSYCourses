package com.sustech.cs309.project.sussycourses.service;


import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.dto.AdminCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;

    public List<AdminCourseDetailResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> new AdminCourseDetailResponse(course.getCourseId(), course.getCourseName(),
                        course.getDescription(), course.getTopic(), course.getCoverImage(),
                        course.getTeacher().getUserId(), course.getTeacher().getFullName(), course.getType(),
                        course.getStatus()))
                .toList();
    }

    public List<AdminCourseDetailResponse> getCoursesByStatus(String status) {
        List<Course> courses = courseRepository.findByStatus(status);
        return courses.stream()
                .map(course -> new AdminCourseDetailResponse(course.getCourseId(), course.getCourseName(),
                        course.getDescription(), course.getTopic(), course.getCoverImage(),
                        course.getTeacher().getUserId(), course.getTeacher().getFullName(), course.getType(),
                        course.getStatus()))
                .toList();
    }

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
