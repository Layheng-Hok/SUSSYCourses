package com.sustech.cs309.project.sussycourses.service;

import com.sustech.cs309.project.sussycourses.domain.Course;
import com.sustech.cs309.project.sussycourses.domain.CourseStudent;
import com.sustech.cs309.project.sussycourses.domain.Rating;
import com.sustech.cs309.project.sussycourses.domain.WebAppUser;
import com.sustech.cs309.project.sussycourses.dto.RatingRequest;
import com.sustech.cs309.project.sussycourses.dto.RatingResponse;
import com.sustech.cs309.project.sussycourses.repository.CourseRepository;
import com.sustech.cs309.project.sussycourses.repository.CourseStudentRepository;
import com.sustech.cs309.project.sussycourses.repository.RatingRepository;
import com.sustech.cs309.project.sussycourses.repository.WebAppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {
    private final RatingRepository ratingRepository;
    private final WebAppUserRepository webAppUserRepository;
    private final CourseRepository courseRepository;
    private final CourseStudentRepository courseStudentRepository;

    public ResponseEntity<String> rateCourse(Long userId, Long courseId, RatingRequest ratingRequest) {
        Optional<CourseStudent> courseStudent = courseStudentRepository.findCourseStudentByStudent_UserIdAndCourse_CourseId(userId, courseId);
        if (courseStudent.isEmpty() || !courseStudent.get().getStatus().equalsIgnoreCase("enrolled")) {
            return ResponseEntity.status(404).body("Student is not enrolled in this course");
        }

        Float contentQuality = ratingRequest.contentQuality();
        Float teachingCompetence = ratingRequest.teachingCompetence();
        Float workloadBalance = ratingRequest.workloadBalance();
        Float overallRating = (contentQuality + teachingCompetence + workloadBalance) / 3;

        Rating rating = new Rating();
        rating.setStudent(webAppUserRepository.findById(userId).orElse(null));
        rating.setCourse(courseRepository.findByCourseId(courseId).orElse(null));
        rating.setOverallRating(overallRating);
        rating.setContentQuality(contentQuality);
        rating.setTeachingCompetence(teachingCompetence);
        rating.setWorkloadBalance(workloadBalance);
        rating.setFeedback(ratingRequest.feedback());
        rating.setCreatedAt(LocalDateTime.now());
        ratingRepository.save(rating);

        Course course = courseStudent.get().getCourse();
        course.setTotalEvaluationScore(course.getTotalEvaluationScore() + overallRating);
        course.setNumEvaluations(course.getNumEvaluations() + 1);
        courseRepository.save(course);

        WebAppUser student = webAppUserRepository.findById(userId).orElse(null);
        assert student != null;
        student.setPoints(student.getPoints() + 20);
        webAppUserRepository.save(student);

        return ResponseEntity.ok("Rating submitted successfully");
    }

    public RatingResponse getRatingByStudentIdAndCourseId(Long userId, Long courseId) {
        Optional<Rating> ratingOptional = ratingRepository.findByStudentIdAndCourseId(userId, courseId);
        if (ratingOptional.isEmpty()) {
            return null;
        }

        Rating rating = ratingOptional.get();
        return new RatingResponse(
                rating.getOverallRating(),
                rating.getContentQuality(),
                rating.getTeachingCompetence(),
                rating.getWorkloadBalance(),
                rating.getFeedback()
        );
    }
}
