package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StreamRepository extends JpaRepository<Stream, Integer> {
    Optional<Stream> findByTeacher_UserId(Long teacherId);

    @Query("SELECT s FROM Stream s WHERE s.teacher.userId = :teacherId")
    Stream findByTeacherId(@Param("teacherId") Long teacherId);
}
