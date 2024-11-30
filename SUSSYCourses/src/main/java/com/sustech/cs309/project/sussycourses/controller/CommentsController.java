package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.CommentCreationRequest;
import com.sustech.cs309.project.sussycourses.dto.CommentResponse;
import com.sustech.cs309.project.sussycourses.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentsController {

    private final CommentService commentService;

    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_INSTRUCTOR')")
    @GetMapping("/users/{userId}/courses/{courseId}/comments")
    public List<CommentResponse> getCommentsByCourseId(@PathVariable Long userId, @PathVariable Long courseId) {
        return commentService.getCommentsByCourseId(userId, courseId);
    }

    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_INSTRUCTOR')")
    @Transactional
    @PostMapping("/users/{userId}/courses/{courseId}/comments/create")
    public ResponseEntity<String> createComment(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @ModelAttribute CommentCreationRequest commentCreationRequest) throws Exception {
        return commentService.createComment(userId, courseId, commentCreationRequest);
    }
}
