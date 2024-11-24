package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.dto.CommentResponse;
import com.sustech.cs309.project.sussycourses.repository.CommentRepository;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentsController {
    private final CommentRepository commentRepository;

    private final CourseRepository courseRepository;

    private final WebAppUserRepository webAppUserRepository;

    private final CommentService commentService;

    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_INSTRUCTOR')")
    @GetMapping("/users/{userId}/courses/{courseId}/comments")
    public List<CommentResponse> getCommentsByCourseId(@PathVariable Long userId, @PathVariable Long courseId) {
        return commentService.getCommentsByCourseId(userId, courseId);
    }

    // Post a new comment
//    @PreAuthorize("hasRole('ROLE_STUDENT')")
//    @Transactional
//    @PostMapping
//    public void addComment(@RequestBody CommentRequest commentRequest) {
//        String message = commentRequest.message();
//        long courseId = commentRequest.courseId();
//        long userId = commentRequest.userId();
//
//        Course course = courseRepository.findByCourseId(courseId).orElse(null);
//        WebAppUser user = webAppUserRepository.findById(userId).orElse(null);
//        Comment comment = new Comment();
//        comment.setCourse(course);
//        comment.setUser(user);
//        comment.setMessage(message);
//        comment.setCreatedAt(LocalDateTime.now());
//        commentRepository.save(comment);
//    }
//
//    @PreAuthorize("hasRole('ROLE_STUDENT')")
//    @Transactional
//    @PostMapping("/reply")
//    public void addReply(@RequestBody ReplyRequest replyRequest) {
//        String message = replyRequest.message();
//        long courseId = replyRequest.courseId();
//        long userId = replyRequest.userId();
//        long commentId = replyRequest.commentId();
//
//        // Validate and fetch related entities
//        Course course = courseRepository.findByCourseId(courseId)
//                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
//        WebAppUser user = webAppUserRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//        String commentToReply = commentRepository.findById(commentId)
//                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId))
//                .getMessage();
//
//        // Create and save the reply
//        Comment comment = new Comment();
//        comment.setCourse(course);
//        comment.setUser(user);
//        comment.setMessage(message);
//        comment.setReply(commentToReply);
//        comment.setReplyId(commentId);
//        comment.setCreatedAt(LocalDateTime.now());
//        commentRepository.save(comment);
//    }

}
