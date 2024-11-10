package com.incture.lms.controllers;

import com.incture.lms.dto.GradeDTO;
import com.incture.lms.exceptions.ResourceNotFoundException;
import com.incture.lms.models.Assignment;
import com.incture.lms.models.Grade;
import com.incture.lms.models.User;
import com.incture.lms.repositories.AssignmentRepository;
import com.incture.lms.repositories.UserRepository;
import com.incture.lms.services.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private static final Logger logger = LoggerFactory.getLogger(GradeController.class);

    @Autowired
    private GradeService gradeService;
    @Autowired
    private UserRepository userRepository; 
    @Autowired
    private AssignmentRepository assignmentRepository; 


    // Get all grades
    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        logger.info("Request to get all grades.");
        List<Grade> grades = gradeService.getAllGrades();
        if (grades.isEmpty()) {
            logger.info("No grades found.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(grades, HttpStatus.OK);
    }

    // Get grade by ID
    @GetMapping("/{gradeId}")
    public ResponseEntity<Grade> getGradeById(@PathVariable("gradeId") Long gradeId) {
        logger.info("Request to get grade with ID: {}", gradeId);
        Optional<Grade> grade = gradeService.getGradeById(gradeId);
        return grade.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create new grade
    @PostMapping
    public ResponseEntity<Grade> createGrade(@RequestBody GradeDTO gradeDTO) {
        logger.info("Request to create grade for student ID: {}, assignment ID: {}, score: {}", 
                    gradeDTO.getStudentId(), gradeDTO.getAssignmentId(), gradeDTO.getGrade());

        // Fetch the student and assignment before creating the grade
        User student = userRepository.findById(gradeDTO.getStudentId())
                                     .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Assignment assignment =assignmentRepository.findById(gradeDTO.getAssignmentId())
                                                   .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        // Create the grade object
        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setAssignment(assignment);
        grade.setGrade(gradeDTO.getGrade());
        grade.setComments(gradeDTO.getComments());
        grade.setGradedAt(gradeDTO.getGradedAt());

        Grade createdGrade = gradeService.saveGrade(grade);
        return new ResponseEntity<>(createdGrade, HttpStatus.CREATED);
    }
 // Update grade by ID
    @PutMapping("/{gradeId}")
    public ResponseEntity<Grade> updateGrade(
            @PathVariable("gradeId") Long gradeId, 
            @RequestBody GradeDTO gradeDTO) {
        
        logger.info("Request to update grade with ID: {}", gradeId);

        // Fetch the existing grade, student, and assignment
        Grade existingGrade = gradeService.getGradeById(gradeId)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found"));

        User student = userRepository.findById(gradeDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Assignment assignment = assignmentRepository.findById(gradeDTO.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        // Update fields
        existingGrade.setStudent(student);
        existingGrade.setAssignment(assignment);
        existingGrade.setGrade(gradeDTO.getGrade());
        existingGrade.setComments(gradeDTO.getComments());
        existingGrade.setGradedAt(gradeDTO.getGradedAt());

        // Save the updated grade
        Grade updatedGrade = gradeService.saveGrade(existingGrade);
        return new ResponseEntity<>(updatedGrade, HttpStatus.OK);
    }


    // Delete grade by ID
    @DeleteMapping("/{gradeId}")
    public ResponseEntity<HttpStatus> deleteGrade(@PathVariable("gradeId") Long gradeId) {
        logger.info("Request to delete grade with ID: {}", gradeId);
        boolean isDeleted = gradeService.deleteGrade(gradeId);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.warn("Grade with ID {} not found for deletion.", gradeId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
