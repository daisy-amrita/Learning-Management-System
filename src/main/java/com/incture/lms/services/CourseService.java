package com.incture.lms.services;

import com.incture.lms.models.Course;
import com.incture.lms.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    // Get all courses
    public List<Course> getAllCourses() {
        logger.info("Fetching all courses.");
        return courseRepository.findAll();
    }

    // Get course by id
    public Optional<Course> getCourseById(Long courseId) {
        logger.info("Fetching course with id: {}", courseId);
        return courseRepository.findById(courseId);
    }

    // Create new course
    public Course saveCourse(Course course) {
        logger.info("Creating new course: {}", course.getName());
        return courseRepository.save(course);
    }

    // Update course
    public Course updateCourse(Long courseId, Course courseDetails) {
        logger.info("Updating course with id: {}", courseId);
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            Course existingCourse = course.get();
            existingCourse.setName(courseDetails.getName());
            existingCourse.setDescription(courseDetails.getDescription());
            existingCourse.setStartDate(courseDetails.getStartDate());
            existingCourse.setEndDate(courseDetails.getEndDate());
            logger.info("Course updated successfully: {}", existingCourse.getName());
            return courseRepository.save(existingCourse);
        }
        logger.warn("Course with id {} not found.", courseId);
        return null;
    }

    // Delete course by id
    public boolean deleteCourse(Long courseId) {
        logger.info("Deleting course with id: {}", courseId);
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
            logger.info("Course deleted successfully.");
            return true;
        }
        logger.warn("Course with id {} not found.", courseId);
        return false;
    }
}
