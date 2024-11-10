package com.incture.lms.controllers;

import com.incture.lms.models.Course;
import com.incture.lms.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    // Get all courses
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        logger.info("Request to get all courses.");
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            logger.info("No courses found.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    // Get course by id
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable("courseId") Long courseId) {
        logger.info("Request to get course with id: {}", courseId);
        Optional<Course> course = courseService.getCourseById(courseId);
        return course.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new course
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        logger.info("Request to create course: {}", course.getName());
        Course createdCourse = courseService.saveCourse(course);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    // Update course
    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateCourse(@PathVariable("courseId") Long courseId, @RequestBody Course courseDetails) {
        logger.info("Request to update course with id: {}", courseId);
        Course updatedCourse = courseService.updateCourse(courseId, courseDetails);
        if (updatedCourse != null) {
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
        }
        logger.warn("Course with id {} not found for update.", courseId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete course by id
    @DeleteMapping("/{courseId}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("courseId") Long courseId) {
        logger.info("Request to delete course with id: {}", courseId);
        boolean isDeleted = courseService.deleteCourse(courseId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.warn("Course with id {} not found for deletion.", courseId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
