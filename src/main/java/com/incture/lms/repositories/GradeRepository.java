package com.incture.lms.repositories;

import com.incture.lms.models.Grade;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {


    Grade findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);
   
}
