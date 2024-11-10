package com.incture.lms.repositories;

import com.incture.lms.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByName(String name);

    
   
}
