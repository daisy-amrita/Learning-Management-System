package com.incture.lms.repositories;

import com.incture.lms.models.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    
    Assignment findByCourseId(Long courseId);
}
