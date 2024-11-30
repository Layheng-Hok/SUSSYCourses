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

    List<Course> findByTeacher_UserId(Long userId);

    Integer countByTeacher_UserId(Long userId);

    List<Course> findByTeacher_UserIdOrderByTotalEvaluationScoreDesc(Long userId);

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
    List<Object[]> findTopRatedCoursesByWeightedRating();

    @Query(value = """
                SELECT
                    u.user_id,
                    AVG(
                        (CASE
                            WHEN num_evaluations > 0 THEN total_evaluation_score / num_evaluations
                            ELSE 0
                        END) * LOG(num_evaluations + 1)
                    ) * LOG(COUNT(CASE WHEN num_evaluations > 0 THEN 1 END) + 1) AS instructor_rating
                FROM course c
                JOIN web_app_user u ON c.teacher_id = u.user_id
                GROUP BY u.user_id
                ORDER BY instructor_rating DESC
                LIMIT 4;
            """, nativeQuery = true)
    List<Object[]> findTopRatedInstructorsByWeightedRating();

    @Query("SELECT AVG(c.totalEvaluationScore / NULLIF(c.numEvaluations, 0)) AS courseAvg " +
            "FROM Course c " +
            "WHERE c.teacher.userId = :teacherId " +
            "AND c.totalEvaluationScore > 0")
    Double getInstructorAverageRating(Long teacherId);
}
