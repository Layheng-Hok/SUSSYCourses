package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findBySender_UserId(Long userId);

    List<Notification> findByReceiver_UserId(Long userId);
}
