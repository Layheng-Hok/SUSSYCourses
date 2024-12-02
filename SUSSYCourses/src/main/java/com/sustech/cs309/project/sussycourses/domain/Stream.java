package com.sustech.cs309.project.sussycourses.domain;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "stream")
public class Stream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "userId", nullable = false)
    private WebAppUser teacher;

    @Column(length = 100, unique = true)
    private String streamKey;

    @Column(length = 300)
    private String url;

    @Column(length = 100)
    private String title;

    @Column(length = 300)
    private String description;
}
