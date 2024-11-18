package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
//@ToString
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long courseId;

    @Column(length = 100, nullable = false)
    private String courseName;

    @Column(length = 300)
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "userId")
    private WebAppUser teacher;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column
    private String coverImage;

    @OneToMany(mappedBy = "course")
    private List<Courseware> coursewares;

    @Column(length = 50, nullable = false)
    private String topic;

    @OneToMany(mappedBy = "course")
    private List<Rating> ratings;

//    @OneToMany(mappedBy = "student")
//    private List<CourseStudent> courseStudents;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
