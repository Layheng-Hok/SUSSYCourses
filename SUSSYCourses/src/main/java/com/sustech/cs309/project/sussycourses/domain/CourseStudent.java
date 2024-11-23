package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "course_student",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id", "student_id"})})
public class CourseStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "userId", nullable = false)
    private WebAppUser student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "courseId", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean liked;
}

