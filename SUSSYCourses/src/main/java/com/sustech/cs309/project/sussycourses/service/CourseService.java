package com.sustech.cs309.project.sussycourses.service;


import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final WebAppUserRepository webAppUserRepository;
    private final NotificationRepository notificationRepository;
    private final RatingRepository ratingRepository;
    private final CoursewareRepository coursewareRepository;
    private final CoursewareStudentRepository coursewareStudentRepository;

    public List<AdminCourseDetailResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> {
                    try {
                        return new AdminCourseDetailResponse(
                                course.getCourseId(),
                                course.getCourseName(),
                                course.getDescription(),
                                course.getTopic(),
                                CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(course.getCourseId(), course.getCoverImage())),
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

    public StudentCourseDetailResponse getCourseDetail(Long courseId) throws IOException {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return null;
        }

        Course course = courseOptional.get();

        return new StudentCourseDetailResponse(
                courseId,
                course.getCourseName(),
                course.getDescription(),
                course.getTopic(),
                CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(
                        course.getCourseId(),
                        course.getCoverImage()
                )),
                course.getTeacher().getUserId(),
                course.getTeacher().getFullName(),
                course.getTeacher().getBio(),
                CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(
                        course.getTeacher().getUserId(),
                        course.getTeacher().getProfilePicture()
                )),
                course.getType(),
                null,
                null,
                course.getLikeCount(),
                course.getNumEvaluations() != 0 ? course.getTotalEvaluationScore() / course.getNumEvaluations() : 0,
                null,
                null,
                course.getCreatedAt()
        );
    }

    public CourseworkDataResponse getCourseworkData(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return null;
        }
        return new CourseworkDataResponse(
                ratingRepository.findAverageContentQualityRatingByCourseId(courseId),
                ratingRepository.findAverageTeachingCompetenceRatingByCourseId(courseId),
                ratingRepository.findAverageWorkloadBalanceRatingByCourseId(courseId),
                ratingRepository.findAverageRatingByCourseId(courseId),
                (coursewareStudentRepository.countAllCoursewaresByCourseIdAndCategoryWithDisplayVersion(courseId, "assignment") != 0) || (coursewareStudentRepository.countAllCoursewaresByCourseIdAndCategoryWithDisplayVersion(courseId, "assignment") != 0) ?
                        (float) (coursewareStudentRepository.countCompletedCoursewaresByCourseIdAndCategoryWithDisplayVersion(courseId, "assignment") + coursewareStudentRepository.countCompletedCoursewaresByCourseIdAndCategoryWithDisplayVersion(courseId, "project")) /
                                (coursewareStudentRepository.countAllCoursewaresByCourseIdAndCategoryWithDisplayVersion(courseId, "assignment") + coursewareStudentRepository.countAllCoursewaresByCourseIdAndCategoryWithDisplayVersion(courseId, "project")) : 0
        );
    }

    public ApprovedCoursesResponse getApprovedCoursesPaginated(Integer page, Integer size, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursesPage = courseRepository.findByStatus("approved", pageable);
        List<CourseStudent> registeredCourses = courseStudentRepository.findByStudent_UserId(userId);
        Set<Long> enrolledCourseIds = registeredCourses.stream()
                .map(courseStudent -> courseStudent.getCourse().getCourseId())
                .collect(Collectors.toSet());

        List<BasicCourseResponse> basicCourseResponses = new ArrayList<>();
        for (Course course : coursesPage) {
            if (enrolledCourseIds.contains(course.getCourseId())) {
                continue;
            }
            try {
                BasicCourseResponse basicCourseResponse = new BasicCourseResponse(
                        course.getCourseId(),
                        course.getCourseName(),
                        course.getDescription(),
                        course.getTopic(),
                        CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(
                                course.getCourseId(),
                                course.getCoverImage())),
                        course.getTeacher().getUserId(),
                        course.getTeacher().getFullName(),
                        course.getTeacher().getEmail(),
                        course.getType(),
                        course.getLikeCount(),
                        course.getNumEvaluations() != 0 ? course.getTotalEvaluationScore() / course.getNumEvaluations() : 0,
                        course.getCreatedAt()
                );
                basicCourseResponses.add(basicCourseResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<Course> courses = courseRepository.findByStatus("approved");
        List<Long> newCourseIds = new ArrayList<>();
        for (Course course : courses) {
            if (!enrolledCourseIds.contains(course.getCourseId())) {
                newCourseIds.add(course.getCourseId());
            }
        }

        return new ApprovedCoursesResponse((long) newCourseIds.size(), basicCourseResponses);
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

    public ResponseEntity<String> createCourse(CourseRequest courseRequest) throws Exception {
        String courseName = courseRequest.courseName();
        Long teacherId = courseRequest.teacherId();
        String description = courseRequest.description();
        String type = courseRequest.type();
        String topic = courseRequest.topic();
        MultipartFile coverImageFile = courseRequest.coverImageFile();
        String fileType = courseRequest.fileType();
        String coverImageName = courseRequest.coverImageName();

        Optional<WebAppUser> teacherOptional = webAppUserRepository.findById(teacherId);
        if (teacherOptional.isEmpty() || !teacherOptional.get().isEnabled()) {
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
        course.setLikeCount(0L);
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

    public ResponseEntity<String> updateCourse(CourseRequest courseRequest, Long courseId) throws Exception {
        String courseName = courseRequest.courseName();
        Long teacherId = courseRequest.teacherId();
        String description = courseRequest.description();
        String type = courseRequest.type();
        String topic = courseRequest.topic();
        MultipartFile coverImageFile = courseRequest.coverImageFile();
        String fileType = courseRequest.fileType();
        String coverImageName = courseRequest.coverImageName();

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Course not found");
        }

        Optional<WebAppUser> teacherOptional = webAppUserRepository.findByUserIdAndRoleRoleId(teacherId, 3);
        if (teacherOptional.isEmpty() || !teacherOptional.get().isEnabled()) {
            return ResponseEntity.status(404).body("Instructor not found");
        }

        Course course = courseOptional.get();
        WebAppUser teacher = teacherOptional.get();

        if (!course.getTeacher().getUserId().equals(teacherId)) {
            return ResponseEntity.status(403).body("Teacher does not have the permission to edit this course");
        }

        course.setCourseName(courseName);
        course.setDescription(description);
        course.setType(type);
        course.setTopic(topic);
        if (coverImageName != null && !coverImageName.trim().isEmpty()) {
            if (course.getCoverImage() != null) {
                CloudUtils.deleteBlob(CloudUtils.resolveCourseCoverImageLocation(courseId, course.getCoverImage()));
            }
            course.setCoverImage(coverImageName);
            String fileLocation = CloudUtils.resolveCourseCoverImageLocation(courseId, coverImageName);
            CloudUtils.putStorageKey(coverImageFile, fileType, fileLocation);
        }
        courseRepository.save(course);

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

    public List<BasicCourseResponse> getCoursesByInstructorId(Long instructorId) {
        List<Course> courses = courseRepository.findByTeacher_UserId(instructorId);
        return courses.stream()
                .map(course -> {
                    try {
                        return new BasicCourseResponse(
                                course.getCourseId(),
                                course.getCourseName(),
                                course.getDescription(),
                                course.getTopic(),
                                CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(course.getCourseId(), course.getCoverImage())),
                                course.getTeacher().getUserId(),
                                course.getTeacher().getFullName(),
                                course.getTeacher().getEmail(),
                                course.getType(),
                                course.getLikeCount(),
                                course.getNumEvaluations() != 0 ? course.getTotalEvaluationScore() / course.getNumEvaluations() : 0,
                                course.getCreatedAt()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .sorted((course1, course2) -> {
                    double avgRating1 = course1.averageRating();
                    double avgRating2 = course2.averageRating();
                    return Double.compare(avgRating2, avgRating1);
                })
                .toList();
    }

    public List<TopRatedCourseResponse> getTopRatedCourses() throws IOException {
        List<Object[]> rawResults = courseRepository.findTopRatedCoursesByWeightedRating();
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

    public List<TopRatedInstructorResponse> getTopRatedInstructors() throws IOException {
        List<Object[]> rawResults = courseRepository.findTopRatedInstructorsByWeightedRating();
        List<TopRatedInstructorResponse> topRatedInstructorResponses = new ArrayList<>();

        for (Object[] rawResult : rawResults) {
            Long instructorId = (Long) rawResult[0];
            Double instructorWeightedRating = (Double) rawResult[1];
            WebAppUser instructor = webAppUserRepository.findByUserId(instructorId).orElse(null);
            List<Course> courses = courseRepository.findByTeacher_UserId(instructorId);
            List<BasicCourseResponse> topRatedCourses = courses.stream()
                    .map(course -> {
                        try {
                            return new BasicCourseResponse(
                                    course.getCourseId(),
                                    course.getCourseName(),
                                    course.getDescription(),
                                    course.getTopic(),
                                    CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(course.getCourseId(), course.getCoverImage())),
                                    course.getTeacher().getUserId(),
                                    course.getTeacher().getFullName(),
                                    course.getTeacher().getEmail(),
                                    course.getType(),
                                    course.getLikeCount(),
                                    course.getNumEvaluations() != 0 ? course.getTotalEvaluationScore() / course.getNumEvaluations() : 0,
                                    course.getCreatedAt()
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .sorted((course1, course2) -> {
                        double avgRating1 = course1.averageRating();
                        double avgRating2 = course2.averageRating();
                        return Double.compare(avgRating2, avgRating1);
                    })
                    .toList();

            assert instructor != null;
            TopRatedInstructorResponse topRatedInstructorResponse = new TopRatedInstructorResponse(
                    instructorId,
                    instructor.getFullName(),
                    instructor.getEmail(),
                    CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(instructorId, instructor.getProfilePicture())),
                    instructor.getGender(),
                    instructor.getRole().getRoleName(),
                    instructor.getBio(),
                    courseRepository.getInstructorAverageRating(instructorId).floatValue(),
                    instructorWeightedRating.floatValue(),
                    courseStudentRepository.countStudentsByTeacherId(instructorId),
                    courseRepository.countByTeacher_UserId(instructorId),
                    topRatedCourses,
                    instructor.getCreatedAt()
            );

            topRatedInstructorResponses.add(topRatedInstructorResponse);
        }

        return topRatedInstructorResponses;
    }
}
