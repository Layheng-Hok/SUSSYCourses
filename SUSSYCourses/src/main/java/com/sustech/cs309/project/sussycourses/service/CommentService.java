package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Comment;
import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.CommentResponse;
import com.sustech.cs309.project.sussycourses.repository.CommentRepository;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.CourseStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.utils.CloudUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final WebAppUserRepository webAppUserRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final CourseRepository courseRepository;

    public List<CommentResponse> getCommentsByCourseId(Long userId, Long courseId) {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByUserId(userId);
        if (webAppUserOptional.isEmpty()) {
            return null;
        }

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return null;
        }

        WebAppUser webAppUser = webAppUserOptional.get();
        Course course = courseOptional.get();

        if (webAppUser.getRole().getRoleId() == 2) {
            if (courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseId(userId, courseId).isEmpty()) {
                return null;
            }
        } else if (webAppUser.getRole().getRoleId() == 3) {
            if (!course.getTeacher().getUserId().equals(userId)) {
                return null;
            }
        }

        List<Comment> comments = commentRepository.findByCourse_CourseId(courseId);
        return comments.stream()
                .map(comment -> {
                    try {
                        return new CommentResponse(
                                comment.getCommentId(),
                                comment.getUser().getFullName(),
                                CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(
                                        comment.getUser().getUserId(),
                                        comment.getUser().getProfilePicture())),
                                comment.getMessage(),
                                CloudUtils.getStorageKey(CloudUtils.resolveCommentAttachmentLocation(
                                        comment.getCommentId(),
                                        comment.getAttachment()
                                )),
                                comment.getCreatedAt(),
                                comment.getReply() != null ? comment.getReply().getCommentId() : null,
                                comment.getReply() != null ? comment.getReply().getUser().getFullName() : null,
                                comment.getReply() != null ? CloudUtils.getStorageKey(CloudUtils.resolveUserProfilePictureLocation(
                                        comment.getReply().getUser().getUserId(),
                                        comment.getReply().getUser().getProfilePicture())) : null,
                                comment.getReply() != null ? comment.getReply().getMessage() : null,
                                comment.getReply() != null ? CloudUtils.getStorageKey(CloudUtils.resolveCommentAttachmentLocation(
                                        comment.getReply().getCommentId(),
                                        comment.getReply().getAttachment())) : null,
                                comment.getReply() != null ? comment.getReply().getCreatedAt() : null
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

//    public List<CommentResponse> findCommentsByCourseId(Long courseId) {
//        List<Comment> comments = commentRepository.findByCourse_CourseId(courseId);
//        return comments.stream()
//                .map(comment -> new CommentResponse(
//                        comment.getCommentId(),
//                        comment.getReplyId(),
//                        comment.getReply(),
//                        comment.getUser().getFullName(),
//                        comment.getMessage(),
//                        comment.getReply(),
//                        comment.getCreatedAt(),
//                        comment.getReplyId() == null ? "" : findWebAppUserByUserId(comment.getReplyId())))
//                .toList();
//    }

    public String findWebAppUserByUserId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        assert comment != null;
        return comment.getUser().getFullName();
    }
}
