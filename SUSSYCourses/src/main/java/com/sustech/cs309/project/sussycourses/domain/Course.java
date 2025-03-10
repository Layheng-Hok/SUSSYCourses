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
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(length = 50, nullable = false)
    private String topic;

    @Column(nullable = false)
    private Float totalEvaluationScore;

    @Column(nullable = false)
    private Integer numEvaluations;

    @Column(nullable = false)
    private Long likeCount;

    @OneToMany(mappedBy = "course")
    private List<Courseware> coursewares;

    @OneToMany(mappedBy = "course")
    private List<Rating> ratings;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
