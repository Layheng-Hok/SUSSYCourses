package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findById(Long courseId);

    @Query("SELECT c FROM Course c JOIN c.teacher u WHERE u.userId = c.teacher.userId")
    List<Course> findCoursesWithTeacherInfo();


    @Query("SELECT COUNT(cs) FROM CourseStudent cs WHERE cs.student.userId = :userId AND cs.status = 'enrolled'")
    int countEnrolledCoursesByUserId(@Param("userId") Long userId);
}
