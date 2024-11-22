package com.sustech.cs309.project.sussycourses.dto;

import jakarta.validation.constraints.*;

public record RatingRequest(
        @NotNull(message = "Content quality rating is required")
        @DecimalMin(value = "0.5", message = "Content quality rating must be at least 0.5")
        @DecimalMax(value = "5.0", message = "Content quality rating cannot exceed 5.0")
        Float contentQuality,

        @NotNull(message = "Teaching competence rating is required")
        @DecimalMin(value = "0.5", message = "Teaching competence rating must be at least 0.5")
        @DecimalMax(value = "5.0", message = "Teaching competence rating cannot exceed 5.0")
        Float teachingCompetence,

        @NotNull(message = "Workload balance rating is required")
        @DecimalMin(value = "0.5", message = "Workload balance rating must be at least 0.5")
        @DecimalMax(value = "5.0", message = "Workload balance rating cannot exceed 5.0")
        Float workloadBalance,

        @NotEmpty(message = "Feedback is required")
        @Size(max = 300, message = "Feedback must not exceed 300 characters")
        String feedback
) {
}
