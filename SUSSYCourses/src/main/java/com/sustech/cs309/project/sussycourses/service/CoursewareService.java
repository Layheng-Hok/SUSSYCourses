package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.controller.CloudController;
import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.Courseware;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.CoursewareRepository;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CoursewareService {
    @Autowired
    private CoursewareRepository coursewareRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    CloudController cloudController;

    public String resolveCoverPhotoLocation(String courseName, String coverPhotoName){
        return "Courses/" + courseName + "/" + coverPhotoName;
    }

    public String resolveCoursewareLocation(String courseName, String url, String fileType){
        if (Objects.equals(fileType, "pdf")){
            return "Courses/" + courseName + "/courseware/pdf/" + url;
        } else if (Objects.equals(fileType, "md")){
            return "Courses/" + courseName + "/courseware/md/" + url;
        } else if (Objects.equals(fileType, "mp4")){
            return "Courses/" + courseName + "/courseware/videos/" + url;
        } else if (Objects.equals(fileType, "jpg") || Objects.equals(fileType, "jpeg") || Objects.equals(fileType, "png")){
            return "Courses/" + courseName + "/courseware/images/" + url;
        }
        return "filenotfound404.jpg";
    }

    public String retrieveCoursewareData() throws IOException {
        List<JSONObject> data = new ArrayList<>();
        List<Course> courses = courseRepository.findCoursesWithTeacherInfo();

        for (Course course : courses) {
            JSONObject courseData = new JSONObject();
            List<Courseware> courseware = coursewareRepository.findCoursewareForCourse(course.getCourseId());

            String courseName = course.getCourseName();
            String coverPhotoName = course.getCoverImage();
            // Basic course information
            courseData.put("id", course.getCourseId().toString());
            courseData.put("name", courseName);
            courseData.put("description", course.getDescription());
            courseData.put("image", cloudController.getStorageKey(resolveCoverPhotoLocation(courseName, coverPhotoName)));
            // Instructor info
            if (course.getTeacher() != null) {
                courseData.put("instructorName", course.getTeacher().getFullName());
                courseData.put("instructorImage", "/assets/Avatars/instructor.jpg");
                courseData.put("instructorBio", course.getTeacher().getBio());
            }

            // Teaching chapters (mock data, adjust as per your real structure)
            JSONArray teachingChapters = new JSONArray();

            JSONArray homeworkChapters = new JSONArray();
            JSONArray projectChapters = new JSONArray();
            for (Courseware c : courseware) {
                String url = c.getUrl();
                String fileType = c.getFileType();
                if (Objects.equals(c.getCategory(), "lecture")) {
                    if (teachingChapters.length() == c.getChapter()) {
                        JSONObject chapter = teachingChapters.getJSONObject(c.getChapter() - 1);
                        JSONArray materials = chapter.getJSONArray("materials");

                        JSONObject material = new JSONObject();
                        String getMaterial = cloudController.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", getMaterial);
                        materials.put(material);
                    } else if (teachingChapters.length() < c.getChapter()) {
                        JSONObject teachingChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = cloudController.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", getMaterial);
                        teachingChapter.put("name", "Chapter " + c.getChapter());
                        materials.put(material);
                        teachingChapter.put("materials", materials);
                        teachingChapters.put(teachingChapter);
                    }
                } else if (Objects.equals(c.getCategory(), "assignment")) {
                    if (teachingChapters.length() == c.getChapter()) {
                        JSONObject chapter = homeworkChapters.getJSONObject(c.getChapter() - 1);
                        JSONArray materials = chapter.getJSONArray("materials");

                        JSONObject material = new JSONObject();
                        String getMaterial = cloudController.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", getMaterial);
                        materials.put(material);
                    } else if (homeworkChapters.length() < c.getChapter()) {
                        JSONObject homeworkChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = cloudController.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", getMaterial);
                        materials.put(material);
                        homeworkChapter.put("name", "Homework " + c.getChapter());
                        homeworkChapter.put("materials", materials);
                    }
                } else if (Objects.equals(c.getCategory(), "project")) {
                    if (projectChapters.length() == c.getChapter()) {
                        JSONObject chapter = projectChapters.getJSONObject(c.getChapter() - 1);
                        JSONArray materials = chapter.getJSONArray("materials");

                        JSONObject material = new JSONObject();
                        String getMaterial = cloudController.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", getMaterial);
                        materials.put(material);
                    } else if (projectChapters.length() < c.getChapter()) {
                        JSONObject projectChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = cloudController.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", getMaterial);
                        materials.put(material);
                        projectChapter.put("name", "Project " + c.getChapter());
                        projectChapter.put("materials", materials);

                    }
                }
            }
            courseData.put("teachingChapters", teachingChapters);
            courseData.put("homeworkChapters", homeworkChapters);
            courseData.put("projectChapters", projectChapters);
            data.add(courseData);
        }

        return data.toString();
    }

}
