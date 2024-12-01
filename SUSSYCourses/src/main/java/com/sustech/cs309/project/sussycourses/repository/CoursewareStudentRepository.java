package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.CoursewareStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CoursewareStudentRepository extends JpaRepository<CoursewareStudent, Long> {
    Optional<CoursewareStudent> findByStudent_UserIdAndCourseware_CoursewareId(Long userId, Long coursewareId);

    @Query("""
            SELECT COUNT(cs) FROM CoursewareStudent cs \
            WHERE cs.student.userId = :studentId \
            AND cs.courseware.course.courseId = :courseId \
            AND cs.courseware.category = :category \
            AND cs.courseware.displayVersion = true
            """)
    Integer countAllCoursewaresByStudentIdCourseIdAndCategoryWithDisplayVersion(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId,
            @Param("category") String category
    );

    @Query("""
            SELECT COUNT(cs) FROM CoursewareStudent cs \
            WHERE cs.student.userId = :studentId \
            AND cs.courseware.course.courseId = :courseId \
            AND cs.courseware.category = :category \
            AND cs.completed = true \
            AND cs.courseware.displayVersion = true
            """)
    Integer countCompletedCoursewaresByStudentIdCourseIdAndCategoryWithDisplayVersion(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId,
            @Param("category") String category
    );
}
