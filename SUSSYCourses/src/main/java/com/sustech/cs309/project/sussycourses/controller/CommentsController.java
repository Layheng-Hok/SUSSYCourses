package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.domain.Comment;
import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.CommentRequest;
import com.sustech.cs309.project.sussycourses.dto.CommentResponse;
import com.sustech.cs309.project.sussycourses.dto.ReplyRequest;
import com.sustech.cs309.project.sussycourses.repository.CommentRepository;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentsController {
    private final CommentRepository commentRepository;

    private final CourseRepository courseRepository;

    private final WebAppUserRepository webAppUserRepository;

    private final CommentService commentService;
    // Simulation data for get and post from frontend
//    private final List<Comment> comments = new ArrayList<>(List.of(
//        new Comment(1, 1, "This is a comment.", "2023-10-01T12:00:00Z", null, 1),
//        new Comment(2, 2, "This is another comment.", "2023-10-02T12:00:00Z", 1, 1)
//    ));

    // Fetch all comments for a specific course
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/{courseId}")
    public List<CommentResponse> getCommentsByCourse(@PathVariable long courseId) {
        Optional<List<CommentResponse>> commentResponses = Optional.ofNullable(commentService.findCommentsByCourseId(courseId));
        return commentResponses.orElse(null);
    }

    // Post a new comment
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping
    public void addComment(@RequestBody CommentRequest commentRequest) {
        String message = commentRequest.message();
        long courseId = commentRequest.courseId();
        long userId = commentRequest.userId();

        Course course = courseRepository.findByCourseId(courseId).orElse(null);
        WebAppUser user = webAppUserRepository.findById(userId).orElse(null);
        Comment comment = new Comment();
        comment.setCourse(course);
        comment.setUser(user);
        comment.setMessage(message);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @PostMapping("/reply")
    public void addReply(@RequestBody ReplyRequest replyRequest) {
        String message = replyRequest.message();
        long courseId = replyRequest.courseId();
        long userId = replyRequest.userId();
        long commentId = replyRequest.commentId();

        Course course = courseRepository.findByCourseId(courseId).orElse(null);
        WebAppUser user = webAppUserRepository.findById(userId).orElse(null);
        String commentToReply = commentRepository.findById((int) commentId).orElse(null).getMessage();
        Comment comment = new Comment();
        comment.setCourse(course);
        comment.setUser(user);
        comment.setMessage(message);
        comment.setReply(commentToReply);
        comment.setReplyId(commentId);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }
}
