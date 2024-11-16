package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Comment;
import com.sustech.cs309.project.sussycourses.dto.CommentResponse;
import com.sustech.cs309.project.sussycourses.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentResponse> findCommentsByCourseId(long courseId) {
        List<Comment> comments = commentRepository.findByCourse_CourseId(courseId);
        return comments.stream()
                .map(comment -> new CommentResponse(comment.getCommentId(), comment.getReplyId(), comment.getReply(), comment.getUser().getFullName(), comment.getMessage(), comment.getReply(), comment.getCreatedAt()))
                .toList();
    }
}
