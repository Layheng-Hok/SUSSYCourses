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
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "sender_email", referencedColumnName = "email", nullable = false)
    private WebAppUser sender;

    @ManyToOne
    @JoinColumn(name = "receiver_email", referencedColumnName = "email", nullable = false)
    private WebAppUser receiver;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
