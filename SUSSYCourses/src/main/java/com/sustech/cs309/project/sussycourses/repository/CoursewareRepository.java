package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.Courseware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CoursewareRepository extends JpaRepository<Courseware, Long> {
    Optional<Courseware> findById(Long id);

    @Query("SELECT cw FROM Courseware cw JOIN cw.course c WHERE c.courseId = :courseId")
    List<Courseware> findCoursewareForCourse(@Param("courseId") long courseId);

    List<Courseware> findByCourse_CourseId(Long courseId);

    @Query("SELECT cw " +
            "FROM Courseware cw " +
            "WHERE cw.course.courseId = :courseId AND cw.displayVersion = TRUE")
    List<Courseware> findOriginalOrLatestVersionByCourseId(@Param("courseId") Long courseId);

    @Query
    List<Courseware> findByVariantOf(Long variantOf);

    @Query("SELECT c FROM Courseware c WHERE c.course.courseId = :courseId AND c.category = :category AND c.chapter = :chapter ORDER BY c.chapter ASC")
    List<Courseware> findByCourseIdAndCategoryAndChapter(Long courseId, String category, Integer chapter);

    @Query("SELECT c FROM Courseware c WHERE c.variantOf = :variantOf AND c.displayVersion = true")
    Courseware findActiveCourseware(Long variantOf);
}