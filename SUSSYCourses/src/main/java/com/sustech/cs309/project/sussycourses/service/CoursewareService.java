package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.Courseware;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.CoursewareRequest;
import com.sustech.cs309.project.sussycourses.dto.CoursewareResponse;
import com.sustech.cs309.project.sussycourses.dto.CoursewareVersionResponse;
import com.sustech.cs309.project.sussycourses.dto.UpdateCoursewareRequest;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.CourseStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.CoursewareRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import com.sustech.cs309.project.sussycourses.utils.CloudUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoursewareService {
    private final CoursewareRepository coursewareRepository;
    private final CourseRepository courseRepository;
    private final WebAppUserRepository webAppUserRepository;
    private final CourseStudentRepository courseStudentRepository;

    public CoursewareResponse findByCoursewareId(Long coursewareId) throws IOException {
        Courseware c = coursewareRepository.findById(coursewareId).orElse(null);
        assert c != null;
        String url = CloudUtils.getStorageKey(resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));

        return new CoursewareResponse(
                c.getCoursewareId(),
                c.getCourse().getCourseId(),
                c.getFileType(), c.getCategory(),
                c.getDownloadable(), c.getChapter(),
                c.getCoursewareOrder(), c.getVariantOf(),
                c.getVersion(),
                url);
    }

    public String resolveCoverPhotoLocation(String courseName, String coverPhotoName) {
        return "Courses/" + courseName + "/" + coverPhotoName;
    }

    public String resolveCoursewareLocation(Long courseId, Long coursewareId) {
        return "Courses/" + courseId + "/courseware/" + coursewareId;
    }

    public String getDisplayedCoursewaresByUserIdAndCourseId(Long userId, Long courseId) throws IOException {
        Optional<WebAppUser> webAppUserOptional = webAppUserRepository.findByUserId(userId);
        if (webAppUserOptional.isEmpty() || !webAppUserOptional.get().isEnabled()) {
            return null;
        }

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return null;
        }

        WebAppUser webAppUser = webAppUserOptional.get();
        Course course = courseOptional.get();

        if (webAppUser.getRole().getRoleName().equalsIgnoreCase("STUDENT")) {
            if (courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseIdAndStatus(userId, courseId, "enrolled").isEmpty()) {
                return null;
            }
        } else if (webAppUser.getRole().getRoleName().equalsIgnoreCase("INSTRUCTOR")) {
            if (!course.getTeacher().getUserId().equals(userId)) {
                return null;
            }
        }

        return retrieveCoursewareData(courseId);
//        List<Courseware> coursewares = coursewareRepository.findByCourse_CourseIdAndDisplayVersion(courseId, true);
//        return coursewares.stream()
//                .map(courseware -> {
//                    try {
//                        return new CoursewareResponse(
//                            courseware.getCoursewareId(),
//                                courseId,
//                                courseware.getFileType(),
//                                courseware.getCategory(),
//                                courseware.getDownloadable(),
//                                courseware.getChapter(),
//                                courseware.getCoursewareOrder(),
//                                courseware.getVariantOf(),
//                                courseware.getVersion(),
//                                CloudUtils.getStorageKey(resolveCoursewareLocation(courseId, courseware.getCoursewareId()))
//                        );
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .toList();
    }

    public ResponseEntity<String> uploadCourseware(CoursewareRequest coursewareRequest) throws Exception {
        long courseId = coursewareRequest.courseId();
        String fileType = coursewareRequest.fileType();
        String category = coursewareRequest.category();
        boolean downloadable = coursewareRequest.downloadable();
        int chapter = coursewareRequest.chapter();
        int order = coursewareRequest.order();
        int version = coursewareRequest.version();
        long variant = coursewareRequest.variant_of();
        MultipartFile file = coursewareRequest.file();

        Courseware courseware = new Courseware();
        Course course = courseRepository.findById(courseId).orElse(null);
        courseware.setCourse(course);
        courseware.setCategory(category);
        courseware.setCoursewareOrder(order);
        courseware.setChapter(chapter);
        courseware.setDownloadable(downloadable);
        courseware.setFileType(fileType);
        courseware.setUrl(file.getName());
        courseware.setVersion(version);
        courseware.setVariantOf(variant);
        courseware.setCreatedAt(LocalDateTime.now());
        String url = resolveCoursewareLocation(courseware.getCourse().getCourseId(), courseware.getCoursewareId());
        CloudUtils.putStorageKey(file, fileType, url);
        coursewareRepository.save(courseware);
        return ResponseEntity.ok().body("Courseware uploaded");
    }

    public String retrieveCoursewareData(Long courseId) throws IOException {
        List<JSONObject> data = new ArrayList<>();
        Course course = courseRepository.findById(courseId).orElse(null);
            JSONObject courseData = new JSONObject();
            List<Courseware> courseware = coursewareRepository.findOriginalOrLatestVersionByCourseId(courseId);
        assert course != null;
        String courseName = course. getCourseName();
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
                if (Objects.equals(c.getCategory(), "lecture")) {
                    if (teachingChapters.length() == c.getChapter()) {
                        JSONObject chapter = teachingChapters.getJSONObject(c.getChapter() - 1);
                        JSONArray materials = chapter.getJSONArray("materials");

                        JSONObject material = new JSONObject();
                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                        material.put("coursewareId", c.getCoursewareId());
                        material.put("variantOf", c.getVariantOf());
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        materials.put(material);
                    } else if (teachingChapters.length() < c.getChapter()) {
                        JSONObject teachingChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                        material.put("coursewareId", c.getCoursewareId());
                        material.put("variantOf", c.getVariantOf());
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        teachingChapter.put("name", "Chapter " + c.getChapter());
                        materials.put(material);
                        teachingChapter.put("materials", materials);
                        teachingChapters.put(teachingChapter);
                    }
                } else if (Objects.equals(c.getCategory(), "assignment")) {
                    if (homeworkChapters.length() == c.getChapter()) {
                        JSONObject chapter = homeworkChapters.getJSONObject(c.getChapter() - 1);
                        JSONArray materials = chapter.getJSONArray("materials");

                        JSONObject material = new JSONObject();
                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                        material.put("coursewareId", c.getCoursewareId());
                        material.put("variantOf", c.getVariantOf());
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        material.put("order", c.getCoursewareOrder());
                        materials.put(material);
                    } else if (homeworkChapters.length() < c.getChapter()) {
                        JSONObject homeworkChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                        material.put("coursewareId", c.getCoursewareId());
                        material.put("variantOf", c.getVariantOf());
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        materials.put(material);
                        homeworkChapter.put("name", "Homework " + c.getChapter());
                        homeworkChapter.put("materials", materials);
                        homeworkChapters.put(homeworkChapter);
                    }
                } else if (Objects.equals(c.getCategory(), "project")) {
                    if (projectChapters.length() == c.getChapter()) {
                        JSONObject chapter = projectChapters.getJSONObject(c.getChapter() - 1);
                        JSONArray materials = chapter.getJSONArray("materials");

                        JSONObject material = new JSONObject();
                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                        material.put("coursewareId", c.getCoursewareId());
                        material.put("variantOf", c.getVariantOf());
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        materials.put(material);
                    } else if (projectChapters.length() < c.getChapter()) {
                        JSONObject projectChapter = new JSONObject();
                        JSONArray materials = new JSONArray();
                        JSONObject material = new JSONObject();

                        String getMaterial = CloudUtils.getStorageKey(resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                        material.put("coursewareId", c.getCoursewareId());
                        material.put("variantOf", c.getVariantOf());
                        material.put("title", c.getUrl());
                        material.put("url", getMaterial);
                        material.put("type", c.getFileType());
                        materials.put(material);
                        projectChapter.put("name", "Project " + c.getChapter());
                        projectChapter.put("materials", materials);
                        projectChapters.put(projectChapter);
                    }
                }
            }
            courseData.put("teachingChapters", teachingChapters);
            courseData.put("homeworkChapters", homeworkChapters);
            courseData.put("projectChapters", projectChapters);
            data.add(courseData);
            return data.toString();
    }

    public ResponseEntity<String> updateCourseware(UpdateCoursewareRequest updateCoursewareRequest) throws Exception {
        Courseware courseware = coursewareRepository.findById(updateCoursewareRequest.coursewareId()).get();
        long courseId = updateCoursewareRequest.courseId();
        String fileType = updateCoursewareRequest.fileType();
        String category = updateCoursewareRequest.category();
        boolean downloadable = updateCoursewareRequest.downloadable();
        int chapter = updateCoursewareRequest.chapter();
        int order = updateCoursewareRequest.order();
        int version = updateCoursewareRequest.version();
        long variant = updateCoursewareRequest.variant_of();
        boolean changeFile = updateCoursewareRequest.changeFile();
        MultipartFile file = updateCoursewareRequest.file();

        Course course = courseRepository.findById(courseId).get();
        courseware.setFileType(fileType);
        courseware.setCategory(category);
        courseware.setDownloadable(downloadable);
        courseware.setChapter(chapter);
        courseware.setCoursewareOrder(order);
        courseware.setVersion(version);
        courseware.setVariantOf(variant);
        if (changeFile) {
            String url = resolveCoursewareLocation(courseware.getCourse().getCourseId(), courseware.getCoursewareId());
            String urlToStore = url.split("/")[url.split("/").length - 1];
            courseware.setUrl(urlToStore);
            CloudUtils.putStorageKey(file, fileType, url);
        }

        //Sort Order Logic
        List<Courseware> coursewareOrders = coursewareRepository.findByCourseIdAndCategoryAndChapter(courseId, category, chapter);
        coursewareOrders.add(courseware.getCoursewareOrder() - 1, courseware);
        for (int i = 0; i < coursewareOrders.size(); i++) {
            coursewareOrders.get(i).setCoursewareOrder(i + 1);
        }

        coursewareRepository.saveAll(coursewareOrders);

        return ResponseEntity.ok("Updated successfully");
    }


    public List<CoursewareVersionResponse> retrieveCoursewareVersions(Long variantOf) throws IOException {
        List<Courseware> list = coursewareRepository.findByVariantOf(variantOf);
        List<CoursewareVersionResponse> coursewareVersionResponseList = new ArrayList<>();
        for (Courseware courseware : list) {
            String url = CloudUtils.getStorageKey(resolveCoursewareLocation(courseware.getCourse().getCourseId(), courseware.getCoursewareId()));
            CoursewareVersionResponse c = new CoursewareVersionResponse(courseware.getCoursewareId(), courseware.getCategory(), url, courseware.getDownloadable(), courseware.getChapter(), courseware.getCoursewareOrder(), courseware.getVariantOf(), courseware.getVersion(), courseware.getDisplayVersion(), courseware.getCreatedAt());
            coursewareVersionResponseList.add(c);
        }
        return coursewareVersionResponseList;
    }

    public ResponseEntity<String> setActive(Long coursewareId) {
        Courseware coursewareToChange = coursewareRepository.findById(coursewareId).get();
        Courseware activeCourseware = coursewareRepository.findActiveCourseware(coursewareToChange.getVariantOf());

        activeCourseware.setDisplayVersion(false);
        coursewareToChange.setDisplayVersion(true);
        coursewareRepository.save(coursewareToChange);
        coursewareRepository.save(activeCourseware);
        return ResponseEntity.ok("Updated Successfully");
    }


    public ResponseEntity<String> deleteCourseware(Long coursewareId) {
        coursewareRepository.deleteById(coursewareId);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
