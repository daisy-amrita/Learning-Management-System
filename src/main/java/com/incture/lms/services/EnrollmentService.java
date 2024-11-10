package com.incture.lms.services;

import com.incture.lms.models.Enrollment;
import com.incture.lms.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // Get all enrollments
    public List<Enrollment> getAllEnrollments() {
        logger.info("Fetching all enrollments.");
        return enrollmentRepository.findAll();
    }

    // Get enrollment by ID
    public Optional<Enrollment> getEnrollmentById(Long enrollmentId) {
        logger.info("Fetching enrollment with ID: {}", enrollmentId);
        return enrollmentRepository.findById(enrollmentId);
    }

    // Create new enrollment
    public Enrollment saveEnrollment(Enrollment enrollment) {
        logger.info("Creating new enrollment for student ID: {}, course ID: {}", 
                    enrollment.getStudent().getId(), enrollment.getCourse().getId());
        return enrollmentRepository.save(enrollment);
    }

    // Delete enrollment by ID
    public boolean deleteEnrollment(Long enrollmentId) {
        logger.info("Deleting enrollment with ID: {}", enrollmentId);
        if (enrollmentRepository.existsById(enrollmentId)) {
            enrollmentRepository.deleteById(enrollmentId);
            logger.info("Enrollment deleted successfully.");
            return true;
        }
        logger.warn("Enrollment with ID {} not found for deletion.", enrollmentId);
        return false;
    }

}
