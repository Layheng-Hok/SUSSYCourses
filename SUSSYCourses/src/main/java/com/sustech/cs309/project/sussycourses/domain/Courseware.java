package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "courseware")
public class Courseware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long coursewareId;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    private Course course;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Boolean downloadable;

    @Column(nullable = false)
    private Integer chapter;

    @Column(nullable = false, updatable = false)
    private Integer coursewareOrder;

    @Column()
    private Long variantOf;

    @Column(nullable = false, updatable = false)
    private Integer version;

    @Column(nullable = false)
    private Boolean displayVersion;

    @OneToMany(mappedBy = "courseware")
    private List<CoursewareStudent> coursewareStudents;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

