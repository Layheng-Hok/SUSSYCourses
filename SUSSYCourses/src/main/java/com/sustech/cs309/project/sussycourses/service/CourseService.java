package com.sustech.cs309.project.sussycourses.service;


import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.AdminCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.dto.CourseCreationRequest;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.utils.CloudUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final WebAppUserRepository webAppUserRepository;

    public List<AdminCourseDetailResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> new AdminCourseDetailResponse(course.getCourseId(), course.getCourseName(),
                        course.getDescription(), course.getTopic(), course.getCoverImage(),
                        course.getTeacher().getUserId(), course.getTeacher().getFullName(), course.getType(),
                        course.getStatus(), course.getCreatedAt()))
                .toList();
    }

    public List<AdminCourseDetailResponse> getCoursesByStatus(String status) {
        List<Course> courses = courseRepository.findByStatus(status);
        return courses.stream()
                .map(course -> new AdminCourseDetailResponse(course.getCourseId(), course.getCourseName(),
                        course.getDescription(), course.getTopic(), course.getCoverImage(),
                        course.getTeacher().getUserId(), course.getTeacher().getFullName(), course.getType(),
                        course.getStatus(), course.getCreatedAt()))
                .toList();
    }

    public ResponseEntity<String> createCourse(CourseCreationRequest courseCreationRequest) throws Exception {
        String courseName = courseCreationRequest.courseName();
        Long teacherId = courseCreationRequest.teacherId();
        String description = courseCreationRequest.description();
        String type = courseCreationRequest.type();
        String topic = courseCreationRequest.topic();
        MultipartFile coverImageFile = courseCreationRequest.coverImageFile();
        String fileType = courseCreationRequest.fileType();
        String coverImageName = courseCreationRequest.coverImageName();

        WebAppUser teacher = webAppUserRepository.findById(teacherId).orElse(null);
        if (teacher == null) {
            return ResponseEntity.status(404).body("Instructor not found");
        }

        Course course = new Course();
        course.setCourseName(courseName);
        course.setDescription(description);
        course.setTeacher(teacher);
        course.setType(type);
        course.setStatus("pending");
        course.setCoverImage(coverImageName);
        course.setTopic(topic);
        course.setCreatedAt(LocalDateTime.now());
        courseRepository.save(course);

        Long courseId = course.getCourseId();
        String fileLocation = CloudUtils.resolveCourseCoverImageLocation(String.valueOf(course.getCourseId()), coverImageName);
        CloudUtils.putStorageKey(coverImageFile, fileType, fileLocation);

        return ResponseEntity.ok("Course created successfully");
    }

    public ResponseEntity<String> updateCourse(CourseCreationRequest courseCreationRequest) throws Exception {
        String courseName = courseCreationRequest.courseName();
        Long teacherId = courseCreationRequest.teacherId();
        String description = courseCreationRequest.description();
        String type = courseCreationRequest.type();
        String topic = courseCreationRequest.topic();
        MultipartFile coverImageFile = courseCreationRequest.coverImageFile();
        String fileType = courseCreationRequest.fileType();
        String coverImageName = courseCreationRequest.coverImageName();

        return ResponseEntity.ok("Course updated successfully");
    }

    public ResponseEntity<String> approveCourse(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);

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

    public ResponseEntity<String> rejectCourse(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();

            if (course.getStatus().equalsIgnoreCase("rejected")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Course is already rejected");
            }

            course.setStatus("rejected");
            courseRepository.save(course);
            return ResponseEntity.ok("Course rejected");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
    }
}
