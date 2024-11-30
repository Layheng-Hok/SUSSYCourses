package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.CoursewareStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoursewareStudentRepository extends JpaRepository<CoursewareStudent, Long> {
    @Query("SELECT COUNT(cs) FROM CoursewareStudent cs " +
            "WHERE cs.student.userId = :studentId " +
            "AND cs.courseware.course.courseId = :courseId " +
            "AND cs.displayVersion = true")
    Integer countAllCoursewaresByStudentIdAndCourseIdWithDisplayVersion(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId
    );

    @Query("SELECT COUNT(cs) FROM CoursewareStudent cs " +
            "WHERE cs.student.userId = :studentId " +
            "AND cs.courseware.course.courseId = :courseId " +
            "AND cs.completed = true " +
            "AND cs.displayVersion = true")
    Integer countCompletedCoursewaresByStudentIdAndCourseIdWithDisplayVersion(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId
    );
}
