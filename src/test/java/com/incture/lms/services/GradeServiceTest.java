package com.incture.lms.services;

import com.incture.lms.models.Grade;
import com.incture.lms.models.User;
import com.incture.lms.models.Assignment;
import com.incture.lms.repositories.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeService gradeService;

    private Grade testGrade;
    private User testStudent;
    private Assignment testAssignment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

     
        testStudent = new User();
        testStudent.setId(1L);
        testStudent.setUsername("amrita");
        testStudent.setEmail("amu@example.com");
        testStudent.setPassword("qwrty");
        testStudent.setRole(User.Role.STUDENT);

   
        testAssignment = new Assignment();
        testAssignment.setId(1L);
        testAssignment.setTitle("Test Assignment");
        testAssignment.setDescription("Description of the test assignment");

     
        testGrade = new Grade();
        testGrade.setId(1L);
        testGrade.setStudent(testStudent); 
        testGrade.setAssignment(testAssignment);  
        testGrade.setGrade(85);  
    }

    @Test
    void testGetAllGrades() {
        when(gradeRepository.findAll()).thenReturn(List.of(testGrade));

        List<Grade> grades = gradeService.getAllGrades();

        assertEquals(1, grades.size());
        assertEquals(testGrade, grades.get(0));
        verify(gradeRepository, times(1)).findAll();
    }

    @Test
    void testGetGradeById_GradeFound() {
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(testGrade));

        Optional<Grade> grade = gradeService.getGradeById(1L);

        assertTrue(grade.isPresent());
        assertEquals(testGrade, grade.get());
        verify(gradeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetGradeById_GradeNotFound() {
        when(gradeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Grade> grade = gradeService.getGradeById(1L);

        assertFalse(grade.isPresent());
        verify(gradeRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveGrade() {
        when(gradeRepository.save(testGrade)).thenReturn(testGrade);

        Grade savedGrade = gradeService.saveGrade(testGrade);

        assertNotNull(savedGrade);
        assertEquals(testGrade, savedGrade);
        verify(gradeRepository, times(1)).save(testGrade);
    }

    @Test
    void testDeleteGrade_GradeFound() {
        when(gradeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(gradeRepository).deleteById(1L);

        boolean isDeleted = gradeService.deleteGrade(1L);

        assertTrue(isDeleted);
        verify(gradeRepository, times(1)).existsById(1L);
        verify(gradeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteGrade_GradeNotFound() {
        when(gradeRepository.existsById(1L)).thenReturn(false);

        boolean isDeleted = gradeService.deleteGrade(1L);

        assertFalse(isDeleted);
        verify(gradeRepository, times(1)).existsById(1L);
        verify(gradeRepository, never()).deleteById(1L);
    }
}
