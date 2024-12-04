package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
import com.sustech.cs309.project.sussycourses.domain.CoursewareStudent;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.CourseProgressResponse;
import com.sustech.cs309.project.sussycourses.repository.CourseStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.CoursewareStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoursewareStudentService {
    private final CoursewareStudentRepository coursewareStudentRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final WebAppUserRepository webAppUserRepository;

    public CourseProgressResponse getCourseProgressByStudentIdAndCourseId(Long studentId, Long courseId) {
        Optional<CourseStudent> courseStudentOptional = courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseIdAndStatus(studentId, courseId, "enrolled");
        if (courseStudentOptional.isEmpty()) {
            return null;
        }

        return new CourseProgressResponse(
                coursewareStudentRepository.countAllCoursewaresByStudentIdCourseIdAndCategoryWithDisplayVersion(studentId, courseId, "lecture"),
                coursewareStudentRepository.countCompletedCoursewaresByStudentIdCourseIdAndCategoryWithDisplayVersion(studentId, courseId, "lecture"),
                coursewareStudentRepository.countAllCoursewaresByStudentIdCourseIdAndCategoryWithDisplayVersion(studentId, courseId, "assignment") + coursewareStudentRepository.countAllCoursewaresByStudentIdCourseIdAndCategoryWithDisplayVersion(studentId, courseId, "project"),
                coursewareStudentRepository.countCompletedCoursewaresByStudentIdCourseIdAndCategoryWithDisplayVersion(studentId, courseId, "assignment") + coursewareStudentRepository.countCompletedCoursewaresByStudentIdCourseIdAndCategoryWithDisplayVersion(studentId, courseId, "project")
        );
    }

    public ResponseEntity<String> setCoursewareCompletedForStudent(Long studentId, Long coursewareId) {
        Optional<CoursewareStudent> coursewareStudentOptional = coursewareStudentRepository.findByStudent_UserIdAndCourseware_CoursewareId(studentId, coursewareId);
        if (coursewareStudentOptional.isEmpty()) {
            return ResponseEntity.status(404).body("The student is not associated with this courseware");
        }

        CoursewareStudent coursewareStudent = coursewareStudentOptional.get();

        if (!coursewareStudent.getCourseware().getDisplayVersion()) {
            return ResponseEntity.status(422).body("Completion can only be set for courseware marked as the display version");
        }

        if (coursewareStudent.getCompleted()) {
            return ResponseEntity.ok("Courseware already set completed");
        }

        coursewareStudent.setCompleted(true);
        coursewareStudentRepository.save(coursewareStudent);

        WebAppUser student = webAppUserRepository.findByUserId(studentId).orElse(null);
        assert student != null;
        student.setPoints(student.getPoints() + 10);
        webAppUserRepository.save(student);

        return ResponseEntity.ok("Courseware set completed");
    }
}
