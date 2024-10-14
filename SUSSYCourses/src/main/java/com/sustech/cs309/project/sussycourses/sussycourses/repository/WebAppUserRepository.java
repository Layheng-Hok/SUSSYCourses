package com.sustech.cs309.project.sussycourses.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.sussycourses.domain.WebAppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebAppUserRepository extends JpaRepository<WebAppUser, Long> {
        Optional<WebAppUser> findByUsername(String username);
}
