package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseStudentRepository extends JpaRepository<CourseStudent, Long> {

    @Query("SELECT cs FROM CourseStudent cs WHERE cs.student.userId = :userId AND cs.status = 'enrolled'")
    List<CourseStudent> findAllCoursesByStudentId(@Param("userId") Long userId);

    @Query("SELECT cs FROM CourseStudent cs WHERE cs.student.userId = :userId AND cs.course.courseId = :courseId")
    Optional<CourseStudent> findByStudentIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId
    );

    @Query("SELECT COUNT(cs) FROM CourseStudent cs WHERE cs.course.courseId = :courseId AND cs.liked = TRUE")
    Long countLikesByCourseId(@Param("courseId") Long courseId);
}
