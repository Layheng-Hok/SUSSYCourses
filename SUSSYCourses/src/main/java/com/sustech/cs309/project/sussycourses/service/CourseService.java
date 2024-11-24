package com.sustech.cs309.project.sussycourses.service;


import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.Notification;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.AdminCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.dto.ApprovedCoursesResponse;
import com.sustech.cs309.project.sussycourses.dto.BasicCourseResponse;
import com.sustech.cs309.project.sussycourses.dto.CourseCreationRequest;
import com.sustech.cs309.project.sussycourses.repository.*;
import com.sustech.cs309.project.sussycourses.utils.CloudUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final WebAppUserRepository webAppUserRepository;
    private final NotificationRepository notificationRepository;
    private final RatingRepository ratingRepository;

    public List<AdminCourseDetailResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> new AdminCourseDetailResponse(
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getDescription(),
                        course.getTopic(),
                        course.getCoverImage(),
                        course.getTeacher().getUserId(),
                        course.getTeacher().getFullName(),
                        course.getType(),
                        course.getStatus(),
                        course.getCreatedAt()))
                .toList();
    }


    public ApprovedCoursesResponse getApprovedCoursesPaginated(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursesPage = courseRepository.findByStatus("approved", pageable);

        List<BasicCourseResponse> courses = coursesPage.stream()
                .map(course -> {

                    try {
                        return new BasicCourseResponse(
                                course.getCourseId(),
                                course.getCourseName(),
                                course.getDescription(),
                                course.getTopic(),
                                CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(
                                        course.getCourseId(), course.getCoverImage())),
                                course.getTeacher().getUserId(),
                                course.getTeacher().getFullName(),
                                course.getTeacher().getEmail(),
                                course.getType(),
                                courseStudentRepository.countLikesByCourseId(course.getCourseId()),
                                ratingRepository.findAverageRatingByCourseId(course.getCourseId()),
                                course.getCreatedAt());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        Long totalApprovedCourses = courseRepository.countByStatus("approved");

        return new ApprovedCoursesResponse(totalApprovedCourses, courses);
    }

    public List<AdminCourseDetailResponse> getAllPendingCourses() {
        List<Course> courses = courseRepository.findByStatus("pending");
        return courses.stream()
                .map(course -> {
                    try {
                        return new AdminCourseDetailResponse(
                                course.getCourseId(),
                                course.getCourseName(),
                                course.getDescription(),
                                course.getTopic(),
                                CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(
                                        course.getCourseId(),
                                        course.getCoverImage())),
                                course.getTeacher().getUserId(),
                                course.getTeacher().getFullName(),
                                course.getType(),
                                course.getStatus(),
                                course.getCreatedAt());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
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

        Optional<WebAppUser> teacherOptional = webAppUserRepository.findById(teacherId);
        if (teacherOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Instructor not found");
        }

        WebAppUser teacher = teacherOptional.get();

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

        WebAppUser admin = webAppUserRepository.findById(2L).orElse(null);

        Notification teacherToAdminNotification = new Notification();
        teacherToAdminNotification.setSender(teacher);
        teacherToAdminNotification.setReceiver(admin);
        teacherToAdminNotification.setSubject("New Course Submission: " + courseName);
        teacherToAdminNotification.setText(String.format("Instructor %s (%s) has submitted a new course titled '%s' for approval. Please review the course details.",
                teacher.getFullName(), teacher.getEmail(), courseName));
        teacherToAdminNotification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(teacherToAdminNotification);

        Notification adminToTeacherNotification = new Notification();
        adminToTeacherNotification.setSender(admin);
        adminToTeacherNotification.setReceiver(teacher);
        adminToTeacherNotification.setSubject("Course Submission Received");
        adminToTeacherNotification.setText(String.format("Your course titled '%s' has been successfully submitted and is pending review by the admin. You will be notified once the review is complete.",
                courseName));
        adminToTeacherNotification.setCreatedAt(LocalDateTime.now());

        Long courseId = course.getCourseId();
        String fileLocation = CloudUtils.resolveCourseCoverImageLocation(courseId, coverImageName);
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
        Optional<Course> courseOptional = courseRepository.findByCourseId(courseId);

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
        Optional<Course> courseOptional = courseRepository.findByCourseId(courseId);

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
