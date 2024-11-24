package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Comment;
import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.CommentCreationRequest;
import com.sustech.cs309.project.sussycourses.dto.CommentResponse;
import com.sustech.cs309.project.sussycourses.repository.CommentRepository;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.CourseStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.utils.CloudUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
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
                                comment.getAttachment(),
                                comment.getAttachmentType(),
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
                                comment.getReply() != null ? comment.getReply().getAttachment() : null,
                                comment.getReply() != null ? comment.getReply().getAttachmentType() : null,
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

    public ResponseEntity<String> createComment(Long userId, Long courseId, CommentCreationRequest commentCreationRequest) throws Exception {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByUserId(userId);
        if (webAppUserOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Course not found");
        }

        WebAppUser webAppUser = webAppUserOptional.get();
        Course course = courseOptional.get();

        if (webAppUser.getRole().getRoleId() == 2) {
            if (courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseId(userId, courseId).isEmpty()) {
                return ResponseEntity.status(403).body("Student does not have access to this course");
            }
        } else if (webAppUser.getRole().getRoleId() == 3) {
            if (!course.getTeacher().getUserId().equals(userId)) {
                return ResponseEntity.status(403).body("Teacher does not have access to this course");
            }
        }

        Comment comment = new Comment();
        comment.setUser(webAppUser);
        comment.setCourse(course);
        comment.setMessage(commentCreationRequest.message());
        comment.setAttachment(commentCreationRequest.attachmentName());
        comment.setAttachmentType(commentCreationRequest.attachmentFileType());
        comment.setReply(commentCreationRequest.replyToId() != null ?
                commentRepository.findById(commentCreationRequest.replyToId()).orElse(null) : null);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        if (commentCreationRequest.attachmentName() != null && !commentCreationRequest.attachmentName().isBlank() &&
                commentCreationRequest.attachmentFileType() != null && !commentCreationRequest.attachmentFileType().isBlank() &&
                commentCreationRequest.attachmentFile() != null) {
            String fileLocation = CloudUtils.resolveCommentAttachmentLocation(
                    comment.getCommentId(),
                    comment.getAttachment());
            CloudUtils.putStorageKey(commentCreationRequest.attachmentFile(), commentCreationRequest.attachmentFileType(), fileLocation);
        }

        return ResponseEntity.ok().body("Comment created successfully");
    }
}
