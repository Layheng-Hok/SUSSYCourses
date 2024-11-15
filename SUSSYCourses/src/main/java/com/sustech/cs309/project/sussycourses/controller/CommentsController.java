package com.sustech.cs309.project.sussycourses.controller;

import com.sustech.cs309.project.sussycourses.domain.Comment;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {


    // Simulation data for get and post from frontend
    private final List<Comment> comments = new ArrayList<>(List.of(
        new Comment(1, 1, "This is a comment.", "2023-10-01T12:00:00Z", null, 1),
        new Comment(2, 2, "This is another comment.", "2023-10-02T12:00:00Z", 1, 1)
    ));

    // Fetch all comments for a specific course
    @GetMapping("/{courseId}")
    public List<Comment> getCommentsByCourse(@PathVariable int courseId) {
        return comments.stream()
            .filter(comment -> comment.getCourseId() == courseId)
            .collect(Collectors.toList());
    }

    // Post a new comment
    @PostMapping
    public Comment addComment(@RequestBody Comment newComment) {
        newComment.setCommentId(comments.size() + 1); // Simulate auto-increment ID
        comments.add(newComment);
        return newComment;
    }
}
