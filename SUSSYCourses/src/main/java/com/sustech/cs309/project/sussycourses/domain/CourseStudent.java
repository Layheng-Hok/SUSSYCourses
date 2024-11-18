package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@ToString
@Entity
@Table(name = "course_student",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"course_id", "student_id"})})
public class CourseStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "userId")
    private WebAppUser student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    private Course course;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Boolean liked;
}

