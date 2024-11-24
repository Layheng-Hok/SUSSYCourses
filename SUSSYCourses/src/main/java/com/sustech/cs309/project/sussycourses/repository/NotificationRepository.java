package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findBySender_UserId(Long userId, Pageable pageable);

    Page<Notification> findByReceiver_UserId(Long userId, Pageable pageable);

    Long countBySender_UserId(Long userId);

    Long countByReceiver_UserId(Long userId);
}
