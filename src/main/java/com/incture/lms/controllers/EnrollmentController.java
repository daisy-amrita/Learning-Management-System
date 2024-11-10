package com.incture.lms.controllers;

import com.incture.lms.exceptions.ResourceNotFoundException;
import com.incture.lms.models.Enrollment;
import com.incture.lms.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    // Get all enrollments
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        logger.info("Request to get all enrollments.");
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        if (enrollments.isEmpty()) {
            logger.info("No enrollments found.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(enrollments, HttpStatus.OK);
    }

    // Get enrollment by ID
    @GetMapping("/{enrollmentId}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable("enrollmentId") Long enrollmentId) {
        logger.info("Request to get enrollment with ID: {}", enrollmentId);
        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(enrollmentId);
        return enrollment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create new enrollment
    @PostMapping
    public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) {
        logger.info("Request to create enrollment for student ID: {}, course ID: {}", 
                    enrollment.getStudent().getId(), enrollment.getCourse().getId());
        Enrollment createdEnrollment = enrollmentService.saveEnrollment(enrollment);
        return new ResponseEntity<>(createdEnrollment, HttpStatus.CREATED);
    }
    @PutMapping("/{enrollmentId}")
    public ResponseEntity<Enrollment> updateEnrollment(
            @PathVariable("enrollmentId") Long enrollmentId, 
            @RequestBody Enrollment enrollmentDetails) {
        
        logger.info("Request to update enrollment with ID: {}", enrollmentId);

        // Fetch the existing enrollment
        Enrollment existingEnrollment = enrollmentService.getEnrollmentById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        // Update fields (example: update course and student if necessary)
        existingEnrollment.setCourse(enrollmentDetails.getCourse());
        existingEnrollment.setStudent(enrollmentDetails.getStudent());

        // Save the updated enrollment
        Enrollment updatedEnrollment = enrollmentService.saveEnrollment(existingEnrollment);
        return new ResponseEntity<>(updatedEnrollment, HttpStatus.OK);
    }

    // Delete enrollment by ID
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<HttpStatus> deleteEnrollment(@PathVariable("enrollmentId") Long enrollmentId) {
        logger.info("Request to delete enrollment with ID: {}", enrollmentId);
        boolean isDeleted = enrollmentService.deleteEnrollment(enrollmentId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.warn("Enrollment with ID {} not found for deletion.", enrollmentId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
