package com.sustech.cs309.project.sussycourses.dto;


import org.springframework.web.multipart.MultipartFile;

/**
 * A DTO for creating a new comment.
 *
 * @param message            the main text of the comment, maximum 300 characters
 * @param attachmentName     the name of the attached file (if any)
 * @param attachmentFileType the type of the attached file (e.g., "png", "pdf")
 * @param attachmentFile     the actual attached file as a multipart upload
 * @param replyToId          the ID of the comment this comment replies to, or null if it's a new thread
 */
public record CommentCreationRequest(
        String message,
        String attachmentName,
        String attachmentFileType,
        MultipartFile attachmentFile,
        Long replyToId
) {
}
