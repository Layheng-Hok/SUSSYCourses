package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private WebAppUser user;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    private Course course;

    @Column(length = 300, nullable = false)
    private String message;

    @Column(length = 300)
    private String reply;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
