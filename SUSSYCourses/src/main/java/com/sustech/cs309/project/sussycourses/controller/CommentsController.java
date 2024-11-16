package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.domain.Comment;
import com.sustech.cs309.project.sussycourses.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class CommentsController {
    @Autowired
    private CommentRepository commentRepository;
    // Simulation data for get and post from frontend
//    private final List<Comment> comments = new ArrayList<>(List.of(
//        new Comment(1, 1, "This is a comment.", "2023-10-01T12:00:00Z", null, 1),
//        new Comment(2, 2, "This is another comment.", "2023-10-02T12:00:00Z", 1, 1)
//    ));

    // Fetch all comments for a specific course
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @GetMapping("/{courseId}")
    public List<Comment> getCommentsByCourse(@PathVariable int courseId) {
        System.out.println(commentRepository.findByCourse_CourseId(courseId));
        return commentRepository.findByCourse_CourseId(courseId);
    }

    // Post a new comment
    @PostMapping
    public void addComment(@RequestBody Comment newComment) {
        commentRepository.save(newComment);
    }
}
