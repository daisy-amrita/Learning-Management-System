package com.incture.lms.controllers;

import com.incture.lms.models.Enrollment;
import com.incture.lms.models.User;
import com.incture.lms.models.Course;
import com.incture.lms.services.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @InjectMocks
    private EnrollmentController enrollmentController;

    private MockMvc mockMvc;
    private Enrollment testEnrollment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();

        // Set up a sample Enrollment object for testing
        testEnrollment = new Enrollment();
        
        // Create and set User (Student)
        User user = new User();
        user.setId(1L);
        user.setUsername("teststudent");

        // Create and set Course
        Course course = new Course();
        course.setId(1L);
        course.setName("Test Course");

       
        testEnrollment.setStudent(user);
        testEnrollment.setCourse(course);
        testEnrollment.setId(1L);  
    }

    @Test
    void testGetAllEnrollments() throws Exception {
        when(enrollmentService.getAllEnrollments()).thenReturn(List.of(testEnrollment));

        mockMvc.perform(get("/api/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testEnrollment.getId())) 
                .andExpect(jsonPath("$[0].student.id").value(testEnrollment.getStudent().getId()))  
                .andExpect(jsonPath("$[0].course.id").value(testEnrollment.getCourse().getId())) 
                .andDo(MockMvcResultHandlers.print());  

        verify(enrollmentService, times(1)).getAllEnrollments();
    }

    @Test
    void testGetAllEnrollments_Empty() throws Exception {
        when(enrollmentService.getAllEnrollments()).thenReturn(List.of());

        mockMvc.perform(get("/api/enrollments"))
                .andExpect(status().isNoContent());

        verify(enrollmentService, times(1)).getAllEnrollments();
    }

    @Test
    void testGetEnrollmentById_Found() throws Exception {
        when(enrollmentService.getEnrollmentById(1L)).thenReturn(Optional.of(testEnrollment));

        mockMvc.perform(get("/api/enrollments/{enrollmentId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testEnrollment.getId()))  
                .andExpect(jsonPath("$.student.id").value(testEnrollment.getStudent().getId()))  
                .andExpect(jsonPath("$.course.id").value(testEnrollment.getCourse().getId())) 
                .andDo(MockMvcResultHandlers.print()); 

        verify(enrollmentService, times(1)).getEnrollmentById(1L);
    }

    @Test
    void testGetEnrollmentById_NotFound() throws Exception {
        when(enrollmentService.getEnrollmentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/enrollments/{enrollmentId}", 1L))
                .andExpect(status().isNotFound());

        verify(enrollmentService, times(1)).getEnrollmentById(1L);
    }


    @Test
    void testDeleteEnrollment_Found() throws Exception {
        when(enrollmentService.deleteEnrollment(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/enrollments/{enrollmentId}", 1L))
                .andExpect(status().isNoContent());

        verify(enrollmentService, times(1)).deleteEnrollment(1L);
    }

    @Test
    void testDeleteEnrollment_NotFound() throws Exception {
        when(enrollmentService.deleteEnrollment(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/enrollments/{enrollmentId}", 1L))
                .andExpect(status().isNotFound());

        verify(enrollmentService, times(1)).deleteEnrollment(1L);
    }
}
