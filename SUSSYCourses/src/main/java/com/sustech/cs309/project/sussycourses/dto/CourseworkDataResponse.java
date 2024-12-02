package com.sustech.cs309.project.sussycourses.dto;

public record CourseworkDataResponse(
        Float contentQualityRating,
        Float teachingCompetenceRating,
        Float workloadBalanceRating,
        Float overallRating,
        Float courseworkCompletion) {
}
