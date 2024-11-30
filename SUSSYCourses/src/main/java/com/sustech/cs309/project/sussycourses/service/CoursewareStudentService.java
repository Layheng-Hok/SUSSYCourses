package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
import com.sustech.cs309.project.sussycourses.dto.CourseProgressResponse;
import com.sustech.cs309.project.sussycourses.repository.CourseStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.CoursewareStudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoursewareStudentService {
    private final CoursewareStudentRepository coursewareStudentRepository;
    private final CourseStudentRepository courseStudentRepository;

    public CourseProgressResponse getCourseProgressByStudentIdAndCourseId(Long studentId, Long courseId) {
        Optional<CourseStudent> courseStudentOptional = courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseIdAndStatus(studentId, courseId, "enrolled");
        if (courseStudentOptional.isEmpty()) {
            return null;
        }

        return new CourseProgressResponse(
                coursewareStudentRepository.countAllCoursewaresByStudentIdAndCourseIdWithDisplayVersion(studentId, courseId),
                coursewareStudentRepository.countCompletedCoursewaresByStudentIdAndCourseIdWithDisplayVersion(studentId, courseId)
        );
    }
}
