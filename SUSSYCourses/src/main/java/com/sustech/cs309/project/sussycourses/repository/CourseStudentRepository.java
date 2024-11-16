package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseStudentRepository extends JpaRepository<CourseStudent, Long> {

    @Query("SELECT COUNT(cs) FROM CourseStudent cs WHERE cs.student.userId = :userId AND cs.status = 'enrolled'")
    int countEnrolledCoursesByStudentId(@Param("userId") Long userId);
}
