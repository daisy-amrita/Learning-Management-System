package com.incture.lms.controllers;

import com.incture.lms.models.Course;
import com.incture.lms.services.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    private MockMvc mockMvc;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();

        course = new Course();
        course.setId(1L);
        course.setName("Course 101");
        course.setDescription("Sample Course");
    }

    @Test
    void testGetAllCourses() throws Exception {
        when(courseService.getAllCourses()).thenReturn(List.of(course));

        mockMvc.perform(get("/api/courses"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("Course 101"))
               .andExpect(jsonPath("$[0].description").value("Sample Course"));
    }

    @Test
    void testGetCourseById() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/api/courses/{courseId}", 1L))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Course 101"))
               .andExpect(jsonPath("$.description").value("Sample Course"));
    }

    @Test
    void testCreateCourse() throws Exception {
        when(courseService.saveCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Course 101\", \"description\":\"Sample Course\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value("Course 101"))
               .andExpect(jsonPath("$.description").value("Sample Course"));
    }

    @Test
    void testUpdateCourse() throws Exception {
        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setName("Course 101");
        updatedCourse.setDescription("Updated Course");

        when(courseService.updateCourse(eq(1L), any(Course.class))).thenReturn(updatedCourse);

        mockMvc.perform(put("/api/courses/{courseId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Course 101\", \"description\":\"Updated Course\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.description").value("Updated Course"))
               .andExpect(jsonPath("$.name").value("Course 101"));
    }

    @Test
    void testDeleteCourse() throws Exception {
        when(courseService.deleteCourse(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/courses/{courseId}", 1L))
               .andExpect(status().isNoContent());
    }
}
