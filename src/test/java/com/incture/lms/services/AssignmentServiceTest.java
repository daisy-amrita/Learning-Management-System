package com.incture.lms.services;

import com.incture.lms.models.Assignment;
import com.incture.lms.repositories.AssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    private Assignment testAssignment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

       
        testAssignment = new Assignment();
        testAssignment.setId(1L);
        testAssignment.setTitle("Assignment 1");
        testAssignment.setDescription("Description of Assignment 1");
        
        testAssignment.setDueDate(LocalDateTime.of(2024, 12, 31, 23, 59));
    }

    @Test
    void testGetAllAssignments() {
        when(assignmentRepository.findAll()).thenReturn(List.of(testAssignment));

        List<Assignment> assignments = assignmentService.getAllAssignments();

        assertEquals(1, assignments.size());
        assertEquals(testAssignment, assignments.get(0));
        verify(assignmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAssignmentById_AssignmentFound() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(testAssignment));

        Optional<Assignment> assignment = assignmentService.getAssignmentById(1L);

        assertTrue(assignment.isPresent());
        assertEquals(testAssignment, assignment.get());
        verify(assignmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAssignmentById_AssignmentNotFound() {
        when(assignmentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Assignment> assignment = assignmentService.getAssignmentById(1L);

        assertFalse(assignment.isPresent());
        verify(assignmentRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveAssignment() {
        when(assignmentRepository.save(testAssignment)).thenReturn(testAssignment);

        Assignment savedAssignment = assignmentService.saveAssignment(testAssignment);

        assertNotNull(savedAssignment);
        assertEquals(testAssignment, savedAssignment);
        verify(assignmentRepository, times(1)).save(testAssignment);
    }

    @Test
    void testDeleteAssignment_AssignmentFound() {
        when(assignmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(assignmentRepository).deleteById(1L);

        boolean isDeleted = assignmentService.deleteAssignment(1L);

        assertTrue(isDeleted);
        verify(assignmentRepository, times(1)).existsById(1L);
        verify(assignmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAssignment_AssignmentNotFound() {
        when(assignmentRepository.existsById(1L)).thenReturn(false);

        boolean isDeleted = assignmentService.deleteAssignment(1L);

        assertFalse(isDeleted);
        verify(assignmentRepository, times(1)).existsById(1L);
        verify(assignmentRepository, never()).deleteById(1L);
    }
}

