package com.sustech.cs309.project.sussycourses.dto;

import org.springframework.web.multipart.MultipartFile;

/* *
 * Send null if none of the profile picture attributes are updated
 * The rest should not be null regardless the values are updated or not
 */
public record UpdateUserRequest(
        String fullName,
        String gender,
        String bio,
        String profilePictureName,
        String fileType,
        MultipartFile profilePicture
) {
}
