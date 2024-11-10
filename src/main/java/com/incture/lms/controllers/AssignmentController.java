package com.incture.lms.controllers;

import com.incture.lms.exceptions.ResourceNotFoundException;
import com.incture.lms.models.Assignment;
import com.incture.lms.services.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    @Autowired
    private AssignmentService assignmentService;

    // Get all assignments
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        logger.info("Request to get all assignments.");
        List<Assignment> assignments = assignmentService.getAllAssignments();
        if (assignments.isEmpty()) {
            logger.info("No assignments found.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }

    // Get assignment by ID
    @GetMapping("/{assignmentId}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable("assignmentId") Long assignmentId) {
        logger.info("Request to get assignment with ID: {}", assignmentId);
        Optional<Assignment> assignment = assignmentService.getAssignmentById(assignmentId);
        return assignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create new assignment
    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        logger.info("Request to create assignment: {}", assignment.getTitle());
        Assignment createdAssignment = assignmentService.saveAssignment(assignment);
        return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
    }
    @PutMapping("/{assignmentId}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable("assignmentId") Long assignmentId, 
            @RequestBody Assignment assignmentDetails) {
        
        logger.info("Request to update assignment with ID: {}", assignmentId);

        // Fetch the existing assignment
        Assignment existingAssignment = assignmentService.getAssignmentById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignmentId));

        // Update assignment fields (e.g., title, description, due date, etc.)
        existingAssignment.setTitle(assignmentDetails.getTitle());
        existingAssignment.setDescription(assignmentDetails.getDescription());
        existingAssignment.setDueDate(assignmentDetails.getDueDate());

        // Save the updated assignment
        Assignment updatedAssignment = assignmentService.saveAssignment(existingAssignment);
        return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
    }

    // Delete assignment by ID
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<HttpStatus> deleteAssignment(@PathVariable("assignmentId") Long assignmentId) {
        logger.info("Request to delete assignment with ID: {}", assignmentId);
        boolean isDeleted = assignmentService.deleteAssignment(assignmentId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.warn("Assignment with ID {} not found for deletion.", assignmentId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
