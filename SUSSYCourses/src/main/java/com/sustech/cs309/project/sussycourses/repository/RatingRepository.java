package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query(value = "SELECT AVG(overall_rating) FROM rating WHERE course_id = :courseId", nativeQuery = true)
    Float findAverageRatingByCourseId(Long courseId);
}
