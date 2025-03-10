package com.sustech.cs309.project.sussycourses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name cannot be longer than 50 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotNull(message = "Role is required")
        Integer roleId,

        @NotBlank(message = "Gender is required")
        String gender
) {
}
