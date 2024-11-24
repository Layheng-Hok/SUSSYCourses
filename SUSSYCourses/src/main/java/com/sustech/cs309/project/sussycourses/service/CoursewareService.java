package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.Courseware;
import com.sustech.cs309.project.sussycourses.dto.CoursewareRequest;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.CoursewareRepository;
import com.sustech.cs309.project.sussycourses.utils.CloudUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoursewareService {
    private final CoursewareRepository coursewareRepository;
    private final CourseRepository courseRepository;

    public String resolveCoverPhotoLocation(String courseName, String coverPhotoName) {
        return "Courses/" + courseName + "/" + coverPhotoName;
    }

    public String resolveCoursewareLocation(String courseName, String url, String fileType) {
        if (Objects.equals(fileType, "pdf")) {
            return "Courses/" + courseName + "/courseware/pdf/" + url;
        } else if (Objects.equals(fileType, "md")) {
            return "Courses/" + courseName + "/courseware/md/" + url;
        } else if (Objects.equals(fileType, "mp4")) {
            return "Courses/" + courseName + "/courseware/videos/" + url;
        } else if (Objects.equals(fileType, "jpg") || Objects.equals(fileType, "jpeg") || Objects.equals(fileType, "png")) {
            return "Courses/" + courseName + "/courseware/images/" + url;
        }
        return "filenotfound404.jpg";
    }

    public ResponseEntity<String> uploadCourseware(CoursewareRequest coursewareRequest) throws Exception {
        long courseId = coursewareRequest.courseId();
        String fileType = coursewareRequest.fileType();
        String category = coursewareRequest.category();
        boolean downloadable = coursewareRequest.downloadable();
        int chapter = coursewareRequest.chapter();
        int order = coursewareRequest.order();
        MultipartFile file = coursewareRequest.file();

        Courseware courseware = new Courseware();
        Course course = courseRepository.findByCourseId(courseId).orElse(null);
        courseware.setCourse(course);
        courseware.setCategory(category);
        courseware.setCoursewareOrder(order);
        courseware.setChapter(chapter);
        courseware.setDownloadable(downloadable);
        courseware.setFileType(fileType);
        courseware.setUrl(file.getName());
        String url = resolveCoursewareLocation(course.getCourseName(), file.getName(), fileType);
        CloudUtils.putStorageKey(file, fileType, url);
        coursewareRepository.save(courseware);
        return ResponseEntity.ok().body("Courseware uploaded");
    }

    public String retrieveCoursewareData() throws IOException {
        List<JSONObject> data = new ArrayList<>();
        List<Course> courses = courseRepository.findCoursesWithTeacherInfo();

        for (Course course : courses) {
            JSONObject courseData = new JSONObject();
            List<Courseware> courseware = coursewareRepository.findByCourse_CourseId(course.getCourseId());

            String courseName = course.getCourseName();
            String coverPhotoName = course.getCoverImage();
            // Basic course information
            courseData.put("id", course.getCourseId().toString());
            courseData.put("name", courseName);
            courseData.put("description", course.getDescription());
            courseData.put("image", CloudUtils.getStorageKey(resolveCoverPhotoLocation(courseName, coverPhotoName)));
            // Instructor info
            courseData.put("instructorName", course.getTeacher().getFullName());
            courseData.put("instructorImage", "/assets/Avatars/instructor.jpg");
            courseData.put("instructorBio", course.getTeacher().getBio());

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
                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        materials.put(material);
                    } else if (teachingChapters.length() < c.getChapter()) {
                        JSONObject teachingChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
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
                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        materials.put(material);
                    } else if (homeworkChapters.length() < c.getChapter()) {
                        JSONObject homeworkChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        materials.put(material);
                        homeworkChapter.put("name", "Homework " + c.getChapter());
                        homeworkChapter.put("materials", materials);
                    }
                } else if (Objects.equals(c.getCategory(), "project")) {
                    if (projectChapters.length() == c.getChapter()) {
                        JSONObject chapter = projectChapters.getJSONObject(c.getChapter() - 1);
                        JSONArray materials = chapter.getJSONArray("materials");

                        JSONObject material = new JSONObject();
                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        materials.put(material);
                    } else if (projectChapters.length() < c.getChapter()) {
                        JSONObject projectChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(courseName, url, fileType));
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
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
