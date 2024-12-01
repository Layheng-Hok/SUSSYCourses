package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "courseware_student",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"courseware_id", "student_id"})})
public class CoursewareStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courseware_id", referencedColumnName = "coursewareId", nullable = false)
    private Courseware courseware;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "userId", nullable = false)
    private WebAppUser student;

    @Column(nullable = false)
    private Boolean completed;
}
