package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query(value = "SELECT AVG(overall_rating) FROM rating WHERE course_id = :courseId", nativeQuery = true)
    Float findAverageRatingByCourseId(Long courseId);

    @Query(value = "SELECT AVG(content_quality) FROM rating WHERE course_id = :courseId", nativeQuery = true)
    Float findAverageContentQualityRatingByCourseId(Long courseId);

    @Query(value = "SELECT AVG(teaching_competence) FROM rating WHERE course_id = :courseId", nativeQuery = true)
    Float findAverageTeachingCompetenceRatingByCourseId(Long courseId);

    @Query(value = "SELECT AVG(workload_balance) FROM rating WHERE course_id = :courseId", nativeQuery = true)
    Float findAverageWorkloadBalanceRatingByCourseId(Long courseId);

    @Query("SELECT r FROM Rating r WHERE r.student.userId = :userId AND r.course.courseId = :courseId")
    Optional<Rating> findByStudentIdAndCourseId(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId
    );
}
