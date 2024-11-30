package com.sustech.cs309.project.sussycourses.service;


import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.Notification;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.*;
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
import java.util.ArrayList;
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
        course.setTotalEvaluationScore(0F);
        course.setNumEvaluations(0);
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

            Notification notification = new Notification();
            notification.setSender(webAppUserRepository.findByUserId(2L).orElse(null));
            notification.setReceiver(course.getTeacher());
            notification.setSubject("Course Approved");
            notification.setText(String.format("Your course \"%s\" has been approved by the admin.", course.getCourseName()));
            notification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);

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

            Notification notification = new Notification();
            notification.setSender(webAppUserRepository.findByUserId(2L).orElse(null));
            notification.setReceiver(course.getTeacher());
            notification.setSubject("Course Rejected");
            notification.setText(String.format("Your course \"%s\" has been rejected by the admin.", course.getCourseName()));
            notification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);

            return ResponseEntity.ok("Course rejected");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
    }

    public List<TopRatedCourseResponse> getTopRatedCourses() throws IOException {
        List<Object[]> rawResults = courseRepository.findTopRatedCoursesByWeightedRatingNative();
        List<TopRatedCourseResponse> topRatedCourseResponses = new ArrayList<>();

        for (Object[] rawResult : rawResults) {
            Long courseId = (Long) rawResult[0];
            Double totalEvaluationScore = (Double) rawResult[1];
            Integer numEvaluations = (Integer) rawResult[2];
            Double averageRating = (Double) rawResult[3];
            Double weightedRating = (Double) rawResult[4];
            Course course = courseRepository.findByCourseId(courseId).orElse(null);

            assert course != null;
            TopRatedCourseResponse topRatedCourseResponse = new TopRatedCourseResponse(
                    courseId,
                    course.getCourseName(),
                    course.getDescription(),
                    course.getTopic(),
                    CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(courseId, course.getCoverImage())),
                    course.getTeacher().getUserId(),
                    course.getTeacher().getFullName(),
                    course.getTeacher().getEmail(),
                    CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(course.getTeacher().getUserId(), course.getTeacher().getProfilePicture())),
                    course.getType(),
                    totalEvaluationScore.floatValue(),
                    numEvaluations,
                    averageRating.floatValue(),
                    weightedRating.floatValue(),
                    course.getCreatedAt()
            );
            topRatedCourseResponses.add(topRatedCourseResponse);
        }

        return topRatedCourseResponses;
    }
}
