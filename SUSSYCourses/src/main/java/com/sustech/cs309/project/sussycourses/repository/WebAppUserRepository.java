package com.sustech.cs309.project.sussycourses.repository;

import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebAppUserRepository extends JpaRepository<WebAppUser, Long> {
    Optional<WebAppUser> findByEmail(String email);

    Optional<WebAppUser> findByUserId(Long userId);

    Optional<WebAppUser> findByUserIdAndRoleRoleId(Long userId, Integer roleId);

    Optional<WebAppUser> findByEmailAndRoleRoleId(String email, Integer roleId);

    Optional<WebAppUser> findByFullName(String username);

    Optional<WebAppUser> findByVerificationToken(String verificationToken);
}
