package com.sustech.cs309.project.sussycourses.service;


import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.dto.AdminCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public ResponseEntity<String> approveCourse(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();

            if (course.getStatus().equalsIgnoreCase("approved")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Course is already approved");
            }

            course.setStatus("approved");
            courseRepository.save(course);
            return ResponseEntity.ok("Course approved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
    }
}
