package com.sustech.cs309.project.sussycourses.dto;

public record RatingResponse(
        Float overallRating,
        Float contentQuality,
        Float teachingCompetence,
        Float workBalance,
        String feedback
) {
}
