package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
import com.sustech.cs309.project.sussycourses.domain.Notification;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.CourseStudentListResponse;
import com.sustech.cs309.project.sussycourses.dto.StudentCourseDetailResponse;
import com.sustech.cs309.project.sussycourses.dto.StudentCourseListResponse;
import com.sustech.cs309.project.sussycourses.dto.StudentDetailResponse;
import com.sustech.cs309.project.sussycourses.repository.*;
import com.sustech.cs309.project.sussycourses.utils.CloudUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseStudentService {
    private final CourseStudentRepository courseStudentRepository;
    private final WebAppUserRepository webAppUserRepository;
    private final CourseRepository courseRepository;
    private final RatingRepository ratingRepository;
    private final NotificationRepository notificationRepository;

    public StudentCourseListResponse getAllCoursesByStudentId(Long userId) {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByUserIdAndRoleRoleId(userId, 2);
        if (webAppUserOptional.isEmpty() || !webAppUserOptional.get().isEnabled()) {
            return null;
        }

        List<CourseStudent> courses = courseStudentRepository.findByStudent_UserId(userId);
        List<StudentCourseDetailResponse> studentCourseDetailResponses = courses.stream()
                .map(courseStudent -> {
                    try {
                        return new StudentCourseDetailResponse(courseStudent.getCourse().getCourseId(),
                                courseStudent.getCourse().getCourseName(),
                                courseStudent.getCourse().getDescription(),
                                courseStudent.getCourse().getTopic(),
                                CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(
                                        String.valueOf(courseStudent.getCourse().getCourseId()),
                                        courseStudent.getCourse().getCoverImage())),
                                courseStudent.getCourse().getTeacher().getUserId(),
                                null,
                                null,
                                null,
                                courseStudent.getCourse().getType(),
                                courseStudent.getStatus(),
                                courseStudent.getLiked(),
                                null,
                                null,
                                courseStudent.getCourse().getCreatedAt());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return new StudentCourseListResponse(
                studentCourseDetailResponses.size(),
                studentCourseDetailResponses
        );
    }

    public CourseStudentListResponse getAllStudentsByCourseId(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findByCourseId(courseId);
        if (courseOptional.isEmpty() || !courseOptional.get().getStatus().equalsIgnoreCase("approved")) {
            return null;
        }

        List<CourseStudent> courseStudents = courseStudentRepository.findByCourse_CourseId(courseId);
        List<StudentDetailResponse> studentDetailResponses = courseStudents.stream()
                .map(courseStudent -> new StudentDetailResponse(
                        courseStudent.getStudent().getUserId(),
                        courseStudent.getStudent().getFullName(),
                        courseStudent.getStudent().getEmail(),
                        null, null, null, null, null, null
                ))
                .toList();

        return new CourseStudentListResponse(studentDetailResponses.size(), studentDetailResponses);
    }


    public StudentCourseDetailResponse getCourseDetailForStudent(Long userId, Long courseId) throws IOException {
        Optional<CourseStudent> courseStudentOptional =
                courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseId(userId, courseId);

        if (courseStudentOptional.isEmpty() ||
                !courseStudentOptional.get().getStatus().equalsIgnoreCase("enrolled")) {
            return null;
        }

        CourseStudent courseStudent = courseStudentOptional.get();
        return new StudentCourseDetailResponse(
                courseId,
                courseStudent.getCourse().getCourseName(),
                courseStudent.getCourse().getDescription(),
                courseStudent.getCourse().getTopic(),
                courseStudent.getCourse().getCoverImage(),
                courseStudent.getCourse().getTeacher().getUserId(),
                courseStudent.getCourse().getTeacher().getFullName(),
                courseStudent.getCourse().getTeacher().getBio(),
                CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(
                        String.valueOf(courseStudent.getCourse().getTeacher().getUserId()),
                        courseStudent.getCourse().getTeacher().getProfilePicture()
                )),
                courseStudent.getCourse().getType(),
                "enrolled",
                courseStudent.getLiked(),
                courseStudentRepository.countLikesByCourseId(courseStudent.getCourse().getCourseId()),
                ratingRepository.findAverageRatingByCourseId(courseStudent.getCourse().getCourseId()),
                courseStudent.getCourse().getCreatedAt()
        );
    }

    public ResponseEntity<String> likeOrUnlikeCourse(Long userId, Long courseId) {
        Optional<CourseStudent> courseStudentOptional =
                courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseId(userId, courseId);

        if (courseStudentOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Student is not enrolled in the course");
        }

        CourseStudent courseStudent = courseStudentOptional.get();
        Boolean isLiked = courseStudent.getLiked();

        courseStudent.setLiked(!isLiked);
        courseStudentRepository.save(courseStudent);

        String message = isLiked
                ? "Course unliked successfully"
                : "Course liked successfully";

        return ResponseEntity.ok(message);
    }

    public ResponseEntity<String> joinCourse(Long userId, Long courseId) {
        Optional<CourseStudent> courseStudentOptional = courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseId(userId, courseId);
        if (courseStudentOptional.isPresent()) {
            return ResponseEntity.status(409).body("User is already enrolled or pending approval for this course");
        }

        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByUserIdAndRoleRoleId(userId, 2);
        if (webAppUserOptional.isEmpty() || !webAppUserOptional.get().isEnabled()) {
            return ResponseEntity.status(404).body("Invalid user");
        }

        Optional<Course> courseOptional = courseRepository.findByCourseId(courseId);
        if (courseOptional.isEmpty() ||
                !courseOptional.get().getStatus().equalsIgnoreCase("approved") ||
                courseOptional.get().getType().equalsIgnoreCase("non-open")) {
            return ResponseEntity.status(404).body("Course not found or not opened");
        }

        WebAppUser webAppUser = webAppUserOptional.get();
        Course course = courseOptional.get();

        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setStudent(webAppUser);
        courseStudent.setCourse(course);
        courseStudent.setLiked(false);
        courseStudent.setCreatedAt(LocalDateTime.now());

        Notification studentToTeacherNotification = new Notification();
        studentToTeacherNotification.setSender(webAppUser);
        studentToTeacherNotification.setReceiver(course.getTeacher());
        studentToTeacherNotification.setCreatedAt(LocalDateTime.now());

        Notification teacherToStudentNotification = new Notification();
        teacherToStudentNotification.setSender(course.getTeacher());
        teacherToStudentNotification.setReceiver(webAppUser);
        teacherToStudentNotification.setCreatedAt(LocalDateTime.now());

        if (course.getType().equalsIgnoreCase("open")) {
            courseStudent.setStatus("enrolled");
            courseStudentRepository.save(courseStudent);

            studentToTeacherNotification.setSubject("New Student Enrollment");
            studentToTeacherNotification.setText(String.format("Student %s (%s) has successfully enrolled in your course: %s.",
                    webAppUser.getFullName(), webAppUser.getEmail(), course.getCourseName()));

            teacherToStudentNotification.setSubject("Enrollment Confirmation");
            teacherToStudentNotification.setText(String.format("You have successfully enrolled in the course: %s, taught by %s.",
                    course.getCourseName(), course.getTeacher().getFullName()));
        } else if (course.getType().equalsIgnoreCase("semi-open")) {
            courseStudent.setStatus("pending");
            courseStudentRepository.save(courseStudent);

            studentToTeacherNotification.setSubject("Course Enrollment Pending Approval");
            studentToTeacherNotification.setText(String.format("Student %s (%s) has requested to join your course: %s. Approval is required.",
                    webAppUser.getFullName(), webAppUser.getEmail(), course.getCourseName()));

            teacherToStudentNotification.setSubject("Enrollment Request Received");
            teacherToStudentNotification.setText(String.format("Your request to join the course: %s, has been sent to the instructor, %s, for approval.",
                    course.getCourseName(), course.getTeacher().getFullName()));
        }

        notificationRepository.save(studentToTeacherNotification);
        notificationRepository.save(teacherToStudentNotification);

        return ResponseEntity.ok("Course registration successful");
    }
}
