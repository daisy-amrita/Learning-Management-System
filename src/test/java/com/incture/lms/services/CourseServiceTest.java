package com.incture.lms.services;

import com.incture.lms.models.Course;
import com.incture.lms.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course();
        course.setId(1L);
        course.setName("Course 101");
        course.setDescription("Sample Course");
    }

    @Test
    void testGetAllCourses() {
        when(courseRepository.findAll()).thenReturn(List.of(course));

        var courses = courseService.getAllCourses();

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals("Course 101", courses.get(0).getName());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetCourseById() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        var result = courseService.getCourseById(1L);

        assertTrue(result.isPresent());
        assertEquals("Course 101", result.get().getName());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveCourse() {
        when(courseRepository.save(course)).thenReturn(course);

        var savedCourse = courseService.saveCourse(course);

        assertNotNull(savedCourse);
        assertEquals("Course 101", savedCourse.getName());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUpdateCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        var updatedCourse = courseService.updateCourse(1L, course);

        assertNotNull(updatedCourse);
        assertEquals("Course 101", updatedCourse.getName());
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testDeleteCourse() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        boolean isDeleted = courseService.deleteCourse(1L);

        assertTrue(isDeleted);
        verify(courseRepository, times(1)).deleteById(1L);
    }
}
