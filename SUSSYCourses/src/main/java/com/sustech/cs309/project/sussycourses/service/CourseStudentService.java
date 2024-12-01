package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.*;
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
    private final NotificationRepository notificationRepository;
    private final CoursewareRepository coursewareRepository;
    private final CoursewareStudentRepository coursewareStudentRepository;

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
                                        courseStudent.getCourse().getCourseId(),
                                        courseStudent.getCourse().getCoverImage())),
                                courseStudent.getCourse().getTeacher().getUserId(),
                                courseStudent.getCourse().getTeacher().getFullName(),
                                courseStudent.getCourse().getTeacher().getBio(),
                                CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(
                                        courseStudent.getCourse().getTeacher().getUserId(),
                                        courseStudent.getCourse().getTeacher().getProfilePicture())),
                                courseStudent.getCourse().getType(),
                                courseStudent.getStatus(),
                                courseStudent.getLiked(),
                                courseStudent.getCourse().getLikeCount(),
                                courseStudent.getCourse().getNumEvaluations() != 0 ? courseStudent.getCourse().getTotalEvaluationScore() / courseStudent.getCourse().getNumEvaluations() : 0,
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

    public CourseStudentListResponse getAllEnrolledStudentsByCourseId(Long courseId, String status) {
        Optional<Course> courseOptional = courseRepository.findByCourseId(courseId);
        if (courseOptional.isEmpty() || !courseOptional.get().getStatus().equalsIgnoreCase("approved")) {
            return null;
        }

        List<CourseStudent> courseStudents = courseStudentRepository.findByCourse_CourseIdAndStatus(courseId, status);
        List<StudentDetailResponse> studentDetailResponses = courseStudents.stream()
                .map(courseStudent -> {
                    try {
                        return new StudentDetailResponse(
                                courseStudent.getStudent().getUserId(),
                                courseStudent.getStudent().getFullName(),
                                courseStudent.getStudent().getEmail(),
                                CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(
                                        courseStudent.getStudent().getUserId(),
                                        courseStudent.getStudent().getProfilePicture())),
                                courseStudent.getStudent().getGender(),
                                courseStudent.getStudent().getRole().getRoleName(),
                                courseStudent.getStudent().getPoints(),
                                courseStudent.getStudent().getBio(),
                                courseStudent.getStudent().getCreatedAt()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
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
                CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(
                        courseStudent.getCourse().getCourseId(),
                        courseStudent.getCourse().getCoverImage()
                )),
                courseStudent.getCourse().getTeacher().getUserId(),
                courseStudent.getCourse().getTeacher().getFullName(),
                courseStudent.getCourse().getTeacher().getBio(),
                CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(
                        courseStudent.getCourse().getTeacher().getUserId(),
                        courseStudent.getCourse().getTeacher().getProfilePicture()
                )),
                courseStudent.getCourse().getType(),
                "enrolled",
                courseStudent.getLiked(),
                courseStudent.getCourse().getLikeCount(),
                courseStudent.getCourse().getNumEvaluations() != 0 ? courseStudent.getCourse().getTotalEvaluationScore() / courseStudent.getCourse().getNumEvaluations() : 0,
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

        Course course = courseRepository.findById(courseId).orElse(null);
        String message;

        assert course != null;
        if (isLiked) {
            course.setLikeCount(course.getLikeCount() - 1);
            message = "Course unliked successfully";
        } else {
            course.setLikeCount(course.getLikeCount() + 1);
            message = "Course liked successfully";
        }

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

    public ResponseEntity<String> addStudent(Long teacherId, Long courseId, String studentEmail) {
        Optional<WebAppUser> teacherOptional = webAppUserRepository.findByUserIdAndRoleRoleId(teacherId, 3);
        if (teacherOptional.isEmpty() || !teacherOptional.get().isEnabled()) {
            return ResponseEntity.status(404).body("Instructor not found");
        }

        Optional<WebAppUser> studentOptional = webAppUserRepository.findByEmailAndRoleRoleId(studentEmail, 2);
        if (studentOptional.isEmpty() || !studentOptional.get().isEnabled()) {
            return ResponseEntity.status(404).body("Student not found");
        }

        Optional<Course> courseOptional = courseRepository.findByCourseId(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Course not found");
        }
        if (!courseOptional.get().getTeacher().getUserId().equals(teacherId)) {
            return ResponseEntity.status(403).body("Teacher does not have permission to add a student to this course");
        }

        WebAppUser teacher = teacherOptional.get();
        WebAppUser student = studentOptional.get();
        Course course = courseOptional.get();

        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setStudent(student);
        courseStudent.setCourse(course);
        courseStudent.setStatus("enrolled");
        courseStudent.setLiked(false);
        courseStudent.setCreatedAt(LocalDateTime.now());
        courseStudentRepository.save(courseStudent);

        Notification teacherToStudentNotification = new Notification();
        teacherToStudentNotification.setSender(teacher);
        teacherToStudentNotification.setReceiver(student);
        teacherToStudentNotification.setSubject("You have been added to a course");
        teacherToStudentNotification.setText(
                String.format(
                        "You have been successfully added to the course '%s' by %s. Please check your course dashboard for more details.",
                        course.getCourseName(),
                        teacher.getFullName()
                )
        );
        teacherToStudentNotification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(teacherToStudentNotification);

        return ResponseEntity.ok().body("Student added successfully");
    }

    public ResponseEntity<String> acceptOrRejectStudent(Long teacherId, Long courseId, Long studentId, String decision) {
        Optional<WebAppUser> teacherOptional = webAppUserRepository.findByUserIdAndRoleRoleId(teacherId, 3);
        if (teacherOptional.isEmpty() || !teacherOptional.get().isEnabled()) {
            return ResponseEntity.status(404).body("Instructor not found");
        }

        Optional<WebAppUser> studentOptional = webAppUserRepository.findByUserIdAndRoleRoleId(studentId, 2);
        if (studentOptional.isEmpty() || !studentOptional.get().isEnabled()) {
            return ResponseEntity.status(404).body("Student not found");
        }

        Optional<Course> courseOptional = courseRepository.findByCourseId(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Course not found");
        }
        if (!courseOptional.get().getTeacher().getUserId().equals(teacherId)) {
            return ResponseEntity.status(403).body("Teacher does not have permission to add a student to this course");
        }

        Optional<CourseStudent> courseStudentOptional = courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseIdAndStatus(studentId, courseId, "pending");
        if (courseStudentOptional.isEmpty()) {
            return ResponseEntity.status(404).body("No pending enrollment request found for this student in the course");
        }

        WebAppUser teacher = teacherOptional.get();
        WebAppUser student = studentOptional.get();
        Course course = courseOptional.get();

        courseStudentOptional.get().setStatus(decision);

        if (decision.equalsIgnoreCase("enrolled")) {
            decision = "accepted";
        }

        Notification teacherToStudentNotification = new Notification();
        teacherToStudentNotification.setSender(teacher);
        teacherToStudentNotification.setReceiver(student);
        teacherToStudentNotification.setSubject(String.format("Course Enrollment Decision for '%s'", courseOptional.get().getCourseName()));
        teacherToStudentNotification.setText(String.format("Your enrollment request for the course '%s' has been %s by instructor %s.", course.getCourseName(), decision, teacher.getFullName()));
        teacherToStudentNotification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(teacherToStudentNotification);

        if (decision.equalsIgnoreCase("accepted")) {
            List<Courseware> coursewares = coursewareRepository.findByCourse_CourseId(courseId);
            for (Courseware courseware : coursewares) {
                CoursewareStudent coursewareStudent = new CoursewareStudent();
                coursewareStudent.setCourseware(courseware);
                coursewareStudent.setStudent(student);
                coursewareStudent.setCompleted(false);
                coursewareStudentRepository.save(coursewareStudent);
            }
        }

        return ResponseEntity.ok(String.format("Student %s", decision));
    }
}
