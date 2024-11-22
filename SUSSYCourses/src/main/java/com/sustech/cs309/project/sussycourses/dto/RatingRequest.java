package com.sustech.cs309.project.sussycourses.dto;

public record RatingRequest(
        Float contentQuality,
        Float teachingCompetence,
        Float workloadBalance,
        String feedback
) {
}
