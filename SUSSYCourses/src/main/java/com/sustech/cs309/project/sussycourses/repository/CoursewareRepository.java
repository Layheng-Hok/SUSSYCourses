package com.sustech.cs309.project.sussycourses.repository;

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

}