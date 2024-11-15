package com.sustech.cs309.project.sussycourses.domain;

public class Comment {
    private int commentId;
    private int userId;
    private String message;
    private String timestamp;
    private Integer replyId;
    private int courseId;

    public Comment() {
    }

    public Comment(int commentId, int userId, String message, String timestamp, Integer replyId, int courseId) {
        this.commentId = commentId;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.replyId = replyId;
        this.courseId = courseId;
    }

    // Getters and Setters
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", replyId=" + replyId +
                ", courseId=" + courseId +
                '}';
    }
}
