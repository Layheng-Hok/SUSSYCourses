package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @Column(name = "courseware_id")
    private Long coursewareId;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    private Course course;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "downloadable", nullable = false)
    private Boolean downloadable;

    @Column(name = "chapter", nullable = false)
    private int chapter;

    @Column(name = "courseware_order", nullable = false, updatable = false)
    private int coursewareOrder;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

