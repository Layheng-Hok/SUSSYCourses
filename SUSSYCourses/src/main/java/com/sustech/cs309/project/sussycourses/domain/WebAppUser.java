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
@Table(name = "web_app_user")
public class WebAppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 50, nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "roleId")
    private Role role;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private String verificationToken;

    @Column(length = 20, nullable = false)
    private String gender;

    @Column
    private String profilePicture;

    @Column(length = 300)
    private String bio;

    @Column
    private Integer points = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
