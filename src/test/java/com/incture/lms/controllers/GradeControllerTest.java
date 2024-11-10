package com.incture.lms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incture.lms.models.Assignment;
import com.incture.lms.models.Grade;
import com.incture.lms.models.User;
import com.incture.lms.repositories.UserRepository;
import com.incture.lms.services.GradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GradeControllerTest {

    @Mock
    private GradeService gradeService;
    @MockBean
    private UserRepository userRepository;


    @InjectMocks
    private GradeController gradeController;

    private MockMvc mockMvc;
    private Grade testGrade;
    private User testStudent;
    private Assignment testAssignment;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gradeController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // Create a test user (student)
        testStudent = new User();
        testStudent.setId(1L);
        testStudent.setUsername("JohnDoe");
        testStudent.setRole(User.Role.STUDENT);

        // Create a test assignment
        testAssignment = new Assignment();
        testAssignment.setId(1L);
        testAssignment.setTitle("Math Assignment");
        testAssignment.setDescription("Solve all problems");

        // Create a Grade object for testing
        testGrade = new Grade();
        testGrade.setId(1L);
        testGrade.setStudent(testStudent);
        testGrade.setAssignment(testAssignment);
        testGrade.setGrade(95);
        testGrade.setComments("Great job!");
        testGrade.setGradedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllGrades() throws Exception {
        when(gradeService.getAllGrades()).thenReturn(List.of(testGrade));

        mockMvc.perform(get("/api/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testGrade.getId()))
                .andExpect(jsonPath("$[0].grade").value(testGrade.getGrade()))
                .andExpect(jsonPath("$[0].student.id").value(testGrade.getStudent().getId()))
                .andExpect(jsonPath("$[0].assignment.id").value(testGrade.getAssignment().getId()))
                .andExpect(jsonPath("$[0].comments").value(testGrade.getComments()))
                .andExpect(jsonPath("$[0].gradedAt").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());

        verify(gradeService, times(1)).getAllGrades();
    }

    @Test
    void testGetGradeById_Found() throws Exception {
        when(gradeService.getGradeById(1L)).thenReturn(Optional.of(testGrade));

        mockMvc.perform(get("/api/grades/{gradeId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testGrade.getId()))
                .andExpect(jsonPath("$.grade").value(testGrade.getGrade()))
                .andExpect(jsonPath("$.student.id").value(testGrade.getStudent().getId()))
                .andExpect(jsonPath("$.assignment.id").value(testGrade.getAssignment().getId()))
                .andExpect(jsonPath("$.comments").value(testGrade.getComments()))
                .andExpect(jsonPath("$.gradedAt").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());

        verify(gradeService, times(1)).getGradeById(1L);
    }

    @Test
    void testGetGradeById_NotFound() throws Exception {
        when(gradeService.getGradeById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/grades/{gradeId}", 1L))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(gradeService, times(1)).getGradeById(1L);
    }

//    @Test
//    void testCreateGrade() throws Exception {
//        when(gradeService.saveGrade(any(Grade.class))).thenReturn(testGrade);
//
//        String gradeJson = objectMapper.writeValueAsString(testGrade);
//
//        mockMvc.perform(post("/api/grades")
//                        .contentType("application/json")
//                        .content(gradeJson))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(testGrade.getId()))
//                .andExpect(jsonPath("$.grade").value(testGrade.getGrade()))
//                .andExpect(jsonPath("$.student_id").value(testGrade.getId()))
//                .andExpect(jsonPath("$.assignment_id").value(testGrade.getId()))
//                .andExpect(jsonPath("$.comments").value(testGrade.getComments()))
//                .andExpect(jsonPath("$.gradedAt").isNotEmpty())
//                .andDo(MockMvcResultHandlers.print());
//
//        verify(gradeService, times(1)).saveGrade(any(Grade.class));
//    }

    @Test
    void testDeleteGrade_Found() throws Exception {
        when(gradeService.deleteGrade(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/grades/{gradeId}", 1L))
                .andExpect(status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

        verify(gradeService, times(1)).deleteGrade(1L);
    }

    @Test
    void testDeleteGrade_NotFound() throws Exception {
        when(gradeService.deleteGrade(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/grades/{gradeId}", 1L))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

        verify(gradeService, times(1)).deleteGrade(1L);
    }
}
