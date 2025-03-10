package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.*;
import com.sustech.cs309.project.sussycourses.dto.CoursewareRequest;
import com.sustech.cs309.project.sussycourses.dto.CoursewareResponse;
import com.sustech.cs309.project.sussycourses.dto.CoursewareVersionResponse;
import com.sustech.cs309.project.sussycourses.dto.UpdateCoursewareRequest;
import com.sustech.cs309.project.sussycourses.repository.*;
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
    private final CoursewareStudentRepository coursewareStudentRepository;

    public CoursewareResponse findByCoursewareId(Long coursewareId) throws IOException {
        Courseware c = coursewareRepository.findById(coursewareId).orElse(null);
        assert c != null;
        String url = CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));

        return new CoursewareResponse(
                c.getCourse().getCourseId(),
                c.getCourse().getType(),
                c.getCoursewareId(),
                c.getUrl(),
                c.getFileType(),
                c.getCategory(),
                c.getDownloadable(),
                c.getChapter(),
                c.getCoursewareOrder(),
                c.getVariantOf(),
                c.getVersion(),
                url,
                null);
    }

    public List<CoursewareResponse> getDisplayedCoursewaresByUserIdAndCourseIdForPublicUser(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty() || !courseOptional.get().getStatus().equalsIgnoreCase("approved")) {
            return null;
        }
        return getDisplayedCoursewaresByUserIdAndCourseIdForStudent(1L, courseId);
    }

    public List<CoursewareResponse> getDisplayedCoursewaresByUserIdAndCourseIdForStudent(Long studentId, Long courseId) {
        Optional<WebAppUser> studentOptional = webAppUserRepository.findByUserId(studentId);
        if (studentOptional.isEmpty() || !studentOptional.get().isEnabled()) {
            return null;
        }

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return null;
        }

        if (studentOptional.get().getRole().getRoleId().equals(2)) {
            if (courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseIdAndStatus(studentId, courseId, "enrolled").isEmpty()) {
                return null;
            }
        }

        List<Courseware> coursewares = coursewareRepository.findByCourse_CourseIdAndDisplayVersion(courseId, true);
        return coursewares.stream()
                .map(courseware -> {
                    try {
                        return new CoursewareResponse(
                                courseId,
                                courseware.getCourse().getType(),
                                courseware.getCoursewareId(),
                                courseware.getUrl(),
                                courseware.getFileType(),
                                courseware.getCategory(),
                                courseware.getDownloadable(),
                                courseware.getChapter(),
                                courseware.getCoursewareOrder(),
                                courseware.getVariantOf(),
                                courseware.getVersion(),
                                CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(courseId, courseware.getCoursewareId())),
                                coursewareStudentRepository.findByStudent_UserIdAndCourseware_CoursewareId(studentId, courseware.getCoursewareId()).isPresent() ?
                                        coursewareStudentRepository.findByStudent_UserIdAndCourseware_CoursewareId(studentId, courseware.getCoursewareId()).get().getCompleted() :
                                        null
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    public String getDisplayedCoursewaresByUserIdAndCourseIdForInstructor(Long userId, Long courseId) throws IOException {
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

        if (webAppUser.getRole().getRoleId().equals(3)) {
            if (!course.getTeacher().getUserId().equals(userId)) {
                return null;
            }
        }

        return retrieveCoursewareData(courseId);
    }

    public ResponseEntity<String> createCourseware(CoursewareRequest coursewareRequest) throws Exception {
        Long courseId = coursewareRequest.courseId();
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Course not found");
        }

        Courseware courseware = new Courseware();
        courseware.setCourse(courseOptional.get());
        courseware.setCategory(coursewareRequest.category());
        courseware.setCoursewareOrder(coursewareRequest.order());
        courseware.setChapter(coursewareRequest.chapter());
        courseware.setDownloadable(coursewareRequest.downloadable());
        courseware.setFileType(coursewareRequest.fileType());
        courseware.setUrl(coursewareRequest.fileName());
        courseware.setVersion(coursewareRequest.version());
        courseware.setVariantOf(coursewareRequest.variantOf());
        courseware.setDisplayVersion(true);
        courseware.setCreatedAt(LocalDateTime.now());
        coursewareRepository.save(courseware);

        courseware.setVariantOf(courseware.getCoursewareId());
        coursewareRepository.save(courseware);

        String fileType = coursewareRequest.fileType();
        MultipartFile file = coursewareRequest.file();
        String url = CloudUtils.resolveCoursewareLocation(courseware.getCourse().getCourseId(), courseware.getCoursewareId());
        CloudUtils.putStorageKey(file, fileType, url);

        List<CourseStudent> courseStudents = courseStudentRepository.findByCourse_CourseIdAndStatus(courseId, "enrolled");
        for (CourseStudent courseStudent : courseStudents) {
            CoursewareStudent coursewareStudent = new CoursewareStudent();
            coursewareStudent.setCourseware(courseware);
            coursewareStudent.setStudent(courseStudent.getStudent());
            coursewareStudent.setCompleted(false);
            coursewareStudentRepository.save(coursewareStudent);
        }

        return ResponseEntity.ok().body("Courseware uploaded");
    }

    public String retrieveCoursewareData(Long courseId) throws IOException {
        List<JSONObject> data = new ArrayList<>();
        Course course = courseRepository.findById(courseId).orElse(null);
        JSONObject courseData = new JSONObject();
        List<Courseware> courseware = coursewareRepository.findOriginalOrLatestVersionByCourseId(courseId);
        assert course != null;
        String courseName = course.getCourseName();
        String coverPhotoName = course.getCoverImage();
        // Basic course information
        courseData.put("id", course.getCourseId().toString());
        courseData.put("name", courseName);
        courseData.put("description", course.getDescription());
        courseData.put("image", CloudUtils.getStorageKey(CloudUtils.resolveCourseCoverImageLocation(courseId, coverPhotoName)));
        // Instructor info
        courseData.put("instructorName", course.getTeacher().getFullName());
        courseData.put("instructorImage", "/assets/Avatars/instructor.jpg");
        courseData.put("instructorBio", course.getTeacher().getBio());

        // Teaching chapters (mock data, adjust as per your real structure)
        JSONArray teachingChapters = new JSONArray();
        JSONArray homeworkChapters = new JSONArray();
        JSONArray projectChapters = new JSONArray();
        JSONArray attachments = new JSONArray();
        for (Courseware c : courseware) {
            if (Objects.equals(c.getCategory(), "lecture")) {
                if (teachingChapters.length() == c.getChapter()) {
                    JSONObject chapter = teachingChapters.getJSONObject(c.getChapter() - 1);
                    JSONArray materials = chapter.getJSONArray("materials");

                    JSONObject material = new JSONObject();
                    String getMaterial = CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                    material.put("coursewareId", c.getCoursewareId());
                    material.put("downloadable", c.getDownloadable());
                    material.put("createdAt", c.getCreatedAt());
                    material.put("variantOf", c.getVariantOf());
                    material.put("title", c.getUrl());
                    material.put("url", getMaterial);
                    material.put("type", c.getFileType());
                    material.put("version", c.getVersion());
                    materials.put(material);
                } else if (teachingChapters.length() < c.getChapter()) {
                    JSONObject teachingChapter = new JSONObject();
                    JSONArray materials = new JSONArray();
                    JSONObject material = new JSONObject();

                    String getMaterial = CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                    material.put("coursewareId", c.getCoursewareId());
                    material.put("downloadable", c.getDownloadable());
                    material.put("createdAt", c.getCreatedAt());
                    material.put("variantOf", c.getVariantOf());
                    material.put("title", c.getUrl());
                    material.put("url", getMaterial);
                    material.put("type", c.getFileType());
                    material.put("version", c.getVersion());
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
                    String getMaterial = CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                    material.put("coursewareId", c.getCoursewareId());
                    material.put("downloadable", c.getDownloadable());
                    material.put("createdAt", c.getCreatedAt());
                    material.put("variantOf", c.getVariantOf());
                    material.put("title", c.getUrl());
                    material.put("url", getMaterial);
                    material.put("type", c.getFileType());
                    material.put("order", c.getCoursewareOrder());
                    material.put("version", c.getVersion());
                    materials.put(material);
                } else if (homeworkChapters.length() < c.getChapter()) {
                    JSONObject homeworkChapter = new JSONObject();
                    JSONArray materials = new JSONArray();
                    JSONObject material = new JSONObject();

                    String getMaterial = CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                    material.put("coursewareId", c.getCoursewareId());
                    material.put("downloadable", c.getDownloadable());
                    material.put("createdAt", c.getCreatedAt());
                    material.put("variantOf", c.getVariantOf());
                    material.put("title", c.getUrl());
                    material.put("url", getMaterial);
                    material.put("type", c.getFileType());
                    material.put("version", c.getVersion());
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
                    String getMaterial = CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                    material.put("coursewareId", c.getCoursewareId());
                    material.put("downloadable", c.getDownloadable());
                    material.put("createdAt", c.getCreatedAt());
                    material.put("variantOf", c.getVariantOf());
                    material.put("title", c.getUrl());
                    material.put("url", getMaterial);
                    material.put("type", c.getFileType());
                    material.put("version", c.getVersion());
                    materials.put(material);
                } else if (projectChapters.length() < c.getChapter()) {
                    JSONObject projectChapter = new JSONObject();
                    JSONArray materials = new JSONArray();
                    JSONObject material = new JSONObject();

                    String getMaterial = CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                    material.put("coursewareId", c.getCoursewareId());
                    material.put("downloadable", c.getDownloadable());
                    material.put("createdAt", c.getCreatedAt());
                    material.put("variantOf", c.getVariantOf());
                    material.put("title", c.getUrl());
                    material.put("url", getMaterial);
                    material.put("type", c.getFileType());
                    material.put("version", c.getVersion());
                    materials.put(material);
                    projectChapter.put("name", "Project " + c.getChapter());
                    projectChapter.put("materials", materials);
                    projectChapters.put(projectChapter);
                }
            } else if (Objects.equals(c.getCategory(), "attachment")) {
                JSONObject attachment = new JSONObject();
                JSONArray materials = new JSONArray();
                JSONObject material = new JSONObject();

                String getMaterial = CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(c.getCourse().getCourseId(), c.getCoursewareId()));
                material.put("coursewareId", c.getCoursewareId());
                material.put("downloadable", c.getDownloadable());
                material.put("createdAt", c.getCreatedAt());
                material.put("variantOf", c.getVariantOf());
                material.put("title", c.getUrl());
                material.put("url", getMaterial);
                material.put("type", c.getFileType());
                material.put("version", c.getVersion());
                materials.put(material);
                attachment.put("name", "Attachment " + c.getChapter());
                attachment.put("materials", materials);
                attachments.put(attachment);
            }
        }
        courseData.put("teachingChapters", teachingChapters);
        courseData.put("homeworkChapters", homeworkChapters);
        courseData.put("projectChapters", projectChapters);
        courseData.put("attachments", attachments);
        data.add(courseData);
        return data.toString();
    }

    public ResponseEntity<String> updateCourseware(UpdateCoursewareRequest updateCoursewareRequest) throws Exception {
        Courseware coursewareOriginal = coursewareRepository.findById(updateCoursewareRequest.coursewareId()).orElse(null);
        List<Courseware> allVersions = coursewareRepository.findBrokenVariants(coursewareOriginal.getCoursewareId());

        Courseware courseware = new Courseware();
        Long courseId = updateCoursewareRequest.courseId();
        String fileType = updateCoursewareRequest.fileType();
        String category = updateCoursewareRequest.category();
        Boolean downloadable = updateCoursewareRequest.downloadable();
        Integer chapter = updateCoursewareRequest.chapter();
        Integer order = updateCoursewareRequest.order();
        Integer version = updateCoursewareRequest.version();
        Long variant = updateCoursewareRequest.variant_of();
        Boolean changeFile = updateCoursewareRequest.changeFile();
        MultipartFile file = updateCoursewareRequest.file();

        courseware.setVersion(version);
        for (Courseware v : allVersions) {
            if (v.getVersion() >= courseware.getVersion()) {
                courseware.setVersion(v.getVersion() + 1);
            }
        }

        Course course = courseRepository.findById(courseId).get();
        courseware.setCourse(course);
        courseware.setFileType(fileType);
        courseware.setCategory(category);
        courseware.setDownloadable(downloadable);
        courseware.setUrl(coursewareOriginal.getUrl());
        courseware.setChapter(chapter);
        courseware.setCoursewareOrder(order);
        courseware.setVariantOf(variant);
        courseware.setDisplayVersion(false);
        courseware.setCreatedAt(LocalDateTime.now());
        coursewareRepository.save(courseware);

        if (changeFile) {
            String url = CloudUtils.resolveCoursewareLocation(courseware.getCourse().getCourseId(), courseware.getCoursewareId());
            String urlToStore = url.split("/")[url.split("/").length - 1];
            courseware.setUrl(urlToStore);
            CloudUtils.putStorageKey(file, fileType, url);
        }

        coursewareRepository.save(courseware);
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
            String url = CloudUtils.getStorageKey(CloudUtils.resolveCoursewareLocation(courseware.getCourse().getCourseId(), courseware.getCoursewareId()));
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

    public ResponseEntity<String> fixDisplay(Long value) {
        List<Courseware> coursewares = coursewareRepository.findBrokenVariants(value);
        for (Courseware c : coursewares) {
            c.setVariantOf(c.getCoursewareId());
            coursewareRepository.save(c);
        }
        return ResponseEntity.ok("Updated Successfully");
    }
}
