package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long courseId;

    @Column(length = 100, nullable = false)
    private String course_name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "userId")
    private WebAppUser teacher;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column
    private String cover_image;

    @Column
    private LocalDateTime create_time = LocalDateTime.now();

    @OneToMany(mappedBy = "course")
    private List<Courseware> coursewares;
}
