package com.incture.lms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.incture.lms.models.Assignment;
import com.incture.lms.services.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AssignmentControllerTest {

    @Mock
    private AssignmentService assignmentService;

    @InjectMocks
    private AssignmentController assignmentController;

    private MockMvc mockMvc;
    private Assignment testAssignment;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(assignmentController).build();

       
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

       
        testAssignment = new Assignment();
        testAssignment.setId(1L);
        testAssignment.setTitle("Testing");
        testAssignment.setDescription("This is a test assignment");
        testAssignment.setDueDate(LocalDateTime.parse("2024-12-31T23:59:59"));
    }

   

    @Test
    void testGetAllAssignments_Empty() throws Exception {
        when(assignmentService.getAllAssignments()).thenReturn(List.of());

        mockMvc.perform(get("/api/assignments"))
                .andExpect(status().isNoContent());

        verify(assignmentService, times(1)).getAllAssignments();
    }

    

    @Test
    void testGetAssignmentById_NotFound() throws Exception {
        when(assignmentService.getAssignmentById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/assignments/{assignmentId}", 1L))
                .andExpect(status().isNotFound());

        verify(assignmentService, times(1)).getAssignmentById(1L);
    }

    

    @Test
    void testDeleteAssignment_Found() throws Exception {
        when(assignmentService.deleteAssignment(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/assignments/{assignmentId}", 1L))
                .andExpect(status().isNoContent());

        verify(assignmentService, times(1)).deleteAssignment(1L);
    }

    @Test
    void testDeleteAssignment_NotFound() throws Exception {
        when(assignmentService.deleteAssignment(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/assignments/{assignmentId}", 1L))
                .andExpect(status().isNotFound());

        verify(assignmentService, times(1)).deleteAssignment(1L);
    }
}
