package com.sustech.cs309.project.sussycourses.dto;

public record ChangePasswordRequest(String currentPassword, String newPassword, String confirmPassword) {
}
