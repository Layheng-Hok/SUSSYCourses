package com.sustech.cs309.project.sussycourses.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long course_id;

    @Column(length = 100, nullable = false)
    private String course_name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "userId")
    private WebAppUser teacher;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column
    private String cover_image;

    @Column
    private LocalDateTime create_time = LocalDateTime.now();

    @OneToMany(mappedBy = "course")
    private List<Courseware> coursewares;

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeacher(WebAppUser teacher) {
        this.teacher = teacher;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }

    public int getCourse_id() {
        return Math.toIntExact(course_id);
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getDescription() {
        return description;
    }

    public WebAppUser getTeacher() {
        return teacher;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getCover_image() {
        return cover_image;
    }

    public LocalDateTime getCreate_time() {
        return create_time;
    }
}
