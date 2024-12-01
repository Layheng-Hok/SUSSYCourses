package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseStudentRepository extends JpaRepository<CourseStudent, Long> {
    List<CourseStudent> findByCourse_CourseIdAndStatus(Long courseId, String status);

    List<CourseStudent> findByStudent_UserId(Long userId);

    List<CourseStudent> findByStudent_UserIdAndStatus(Long userId, String enrolled);

    Optional<CourseStudent> findCourseStudentByStudent_UserIdAndCourse_CourseId(Long userId, Long courseId);

    Optional<CourseStudent> findCourseStudentByStudent_UserIdAndCourse_CourseIdAndStatus(Long userId, Long courseId, String status);

    @Query("SELECT COUNT(cs) FROM CourseStudent cs WHERE cs.course.courseId = :courseId AND cs.liked = TRUE")
    Long countLikesByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(cs.student.userId) FROM CourseStudent cs " +
            "JOIN cs.course c " +
            "WHERE c.teacher.userId = :teacherId")
    Long countStudentsByTeacherId(@Param("teacherId") Long teacherId);
}
