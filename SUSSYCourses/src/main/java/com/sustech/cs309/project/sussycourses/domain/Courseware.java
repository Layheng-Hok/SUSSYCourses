package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "courseware")
public class Courseware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseware_id")
    private Long coursewareId;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "downloadable", nullable = false)
    private Boolean downloadable;

    @Column(name = "chapter", nullable = false)
    private int chapter;

    @Column(name = "courseware_order", nullable = false, updatable = false)
    private int coursewareOrder;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void setCoursewareId(Long coursewareId) {
        this.coursewareId = coursewareId;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public void setCoursewareOrder(int coursewareOrder) {
        this.coursewareOrder = coursewareOrder;
    }

    public int getCoursewareOrder() {
        return coursewareOrder;
    }

    public int getChapter() {
        return chapter;
    }

    public Course getCourse() {
        return course;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCoursewareId() {
        return coursewareId;
    }

    public String getFileType() {
        return fileType;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getDownloadable() {
        return downloadable;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

