package com.sustech.cs309.project.sussycourses.dto;

public record ReplyRequest (
        Long commentId,
        String message,
        Long userId,
        Long courseId){
}
