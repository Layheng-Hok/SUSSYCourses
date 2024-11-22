package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.RatingRequest;
import com.sustech.cs309.project.sussycourses.dto.RatingResponse;
import com.sustech.cs309.project.sussycourses.service.RatingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @Transactional
    @PutMapping("/students/{userId}/courses/{courseId}/rate")
    public ResponseEntity<String> rateCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @RequestBody @Valid RatingRequest ratingRequest) {
        return ratingService.rateCourse(userId, courseId, ratingRequest);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/students/{userId}/courses/{courseId}/rating")
    public RatingResponse getRatingByStudentIdAndCourseId(@PathVariable Long userId, @PathVariable Long courseId) {
        return ratingService.getRatingByStudentIdAndCourseId(userId, courseId);
    }
}
