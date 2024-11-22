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
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ratingId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "userId", nullable = false)
    private WebAppUser student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "courseId", nullable = false)
    private Course course;

    @Column(nullable = false)
    private Float overallRating;

    @Column(nullable = false)
    private Float contentQuality;

    @Column(nullable = false)
    private Float teachingCompetence;

    @Column(nullable = false)
    private Float workloadBalance;

    @Column(nullable = false, length = 300)
    private String feedback;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
