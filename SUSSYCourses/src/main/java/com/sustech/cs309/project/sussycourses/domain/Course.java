package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "courses")
public class Course {
    @Id
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private String description;

    @Column
    private String status;

    @Column
    private int teacher_id;

    @Column
    private LocalDateTime create_time = LocalDateTime.now();
}
