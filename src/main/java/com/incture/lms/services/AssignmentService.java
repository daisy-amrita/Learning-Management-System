package com.incture.lms.services;

import com.incture.lms.models.Assignment;
import com.incture.lms.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);

    @Autowired
    private AssignmentRepository assignmentRepository;

    // Get all assignments
    public List<Assignment> getAllAssignments() {
        logger.info("Fetching all assignments.");
        return assignmentRepository.findAll();
    }

    // Get assignment by ID
    public Optional<Assignment> getAssignmentById(Long assignmentId) {
        logger.info("Fetching assignment with ID: {}", assignmentId);
        return assignmentRepository.findById(assignmentId);
    }

    // Create new assignment
    public Assignment saveAssignment(Assignment assignment) {
        logger.info("Creating new assignment: {}", assignment.getTitle());
        return assignmentRepository.save(assignment);
    }

    // Delete assignment by ID
    public boolean deleteAssignment(Long assignmentId) {
        logger.info("Deleting assignment with ID: {}", assignmentId);
        if (assignmentRepository.existsById(assignmentId)) {
            assignmentRepository.deleteById(assignmentId);
            logger.info("Assignment deleted successfully.");
            return true;
        }
        logger.warn("Assignment with ID {} not found for deletion.", assignmentId);
        return false;
    }
}
