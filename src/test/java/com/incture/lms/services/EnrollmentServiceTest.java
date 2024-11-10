package com.incture.lms.services;

import com.incture.lms.models.Enrollment;
import com.incture.lms.models.User;
import com.incture.lms.models.Course;
import com.incture.lms.repositories.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment testEnrollment;
    private User testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Set up test data without constructors
        testStudent = new User();
        testStudent.setId(1L);
        testStudent.setUsername("student_user");
        testStudent.setPassword("password");
        testStudent.setEmail("student@example.com");
        testStudent.setRole(User.Role.STUDENT);

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Mathematics");
        testCourse.setDescription("Math basics");
        testCourse.setInstructor(testStudent); // Assuming the instructor is a User object

        testEnrollment = new Enrollment();
        testEnrollment.setId(1L);
        testEnrollment.setStudent(testStudent);
        testEnrollment.setCourse(testCourse);
    }

    @Test
    void testGetAllEnrollments() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(testEnrollment));

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        assertEquals(1, enrollments.size());
        assertEquals(testEnrollment, enrollments.get(0));
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentById_EnrollmentFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(testEnrollment));

        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(1L);

        assertTrue(enrollment.isPresent());
        assertEquals(testEnrollment, enrollment.get());
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEnrollmentById_EnrollmentNotFound() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(1L);

        assertFalse(enrollment.isPresent());
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveEnrollment() {
        when(enrollmentRepository.save(testEnrollment)).thenReturn(testEnrollment);

        Enrollment savedEnrollment = enrollmentService.saveEnrollment(testEnrollment);

        assertNotNull(savedEnrollment);
        assertEquals(testEnrollment, savedEnrollment);
        verify(enrollmentRepository, times(1)).save(testEnrollment);
    }

    @Test
    void testDeleteEnrollment_EnrollmentFound() {
        when(enrollmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(enrollmentRepository).deleteById(1L);

        boolean isDeleted = enrollmentService.deleteEnrollment(1L);

        assertTrue(isDeleted);
        verify(enrollmentRepository, times(1)).existsById(1L);
        verify(enrollmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEnrollment_EnrollmentNotFound() {
        when(enrollmentRepository.existsById(1L)).thenReturn(false);

        boolean isDeleted = enrollmentService.deleteEnrollment(1L);

        assertFalse(isDeleted);
        verify(enrollmentRepository, times(1)).existsById(1L);
        verify(enrollmentRepository, never()).deleteById(1L);
    }
}
