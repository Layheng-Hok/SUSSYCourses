package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseId(Long courseId);

    List<Course> findByStatus(String status);

    Page<Course> findByStatus(String status, Pageable pageable);

    Long countByStatus(String status);

    @Query("SELECT c FROM Course c JOIN c.teacher u WHERE u.userId = c.teacher.userId")
    List<Course> findCoursesWithTeacherInfo();

    List<Course> findByTeacher_UserId(Long userId);

    @Query(value = """
                SELECT c.course_id AS courseId,
                       c.total_evaluation_score AS totalEvaluationScore,
                       c.num_evaluations AS numEvaluations,
                       CASE WHEN c.num_evaluations > 0 THEN c.total_evaluation_score / c.num_evaluations ELSE 0 END AS averageRating,
                       (CASE WHEN c.num_evaluations > 0 THEN c.total_evaluation_score / c.num_evaluations ELSE 0 END) * LOG(c.num_evaluations + 1) AS weightedRating
                FROM course c
                ORDER BY weightedRating DESC
                LIMIT 8
            """, nativeQuery = true)
    List<Object[]> findTopRatedCoursesByWeightedRatingNative();

}
