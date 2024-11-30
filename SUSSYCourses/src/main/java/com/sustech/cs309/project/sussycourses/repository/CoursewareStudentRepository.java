package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.CoursewareStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursewareStudentRepository extends JpaRepository<CoursewareStudent, Long> {
}
