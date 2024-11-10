package com.incture.lms.services;

import com.incture.lms.models.Grade;
import com.incture.lms.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    private static final Logger logger = LoggerFactory.getLogger(GradeService.class);

    @Autowired
    private GradeRepository gradeRepository;

    // Get all grades
    public List<Grade> getAllGrades() {
        logger.info("Fetching all grades.");
        return gradeRepository.findAll();
    }

    // Get grade by ID
    public Optional<Grade> getGradeById(Long gradeId) {
        logger.info("Fetching grade with ID: {}", gradeId);
        return gradeRepository.findById(gradeId);
    }

    // Create new grade
    public Grade saveGrade(Grade grade) {
        logger.info("Saving new grade for student ID: {}, assignment ID: {}, score: {}", 
                    grade.getStudent().getId(), grade.getAssignment().getId(), grade.getGrade());
        return gradeRepository.save(grade);
    }

    // Delete grade by ID
    public boolean deleteGrade(Long gradeId) {
        logger.info("Deleting grade with ID: {}", gradeId);
        if (gradeRepository.existsById(gradeId)) {
            gradeRepository.deleteById(gradeId);
            logger.info("Grade deleted successfully.");
            return true;
        }
        logger.warn("Grade with ID {} not found for deletion.", gradeId);
        return false;
    }
}
