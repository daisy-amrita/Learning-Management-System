package com.incture.lms.services;

import com.incture.lms.models.User;
import com.incture.lms.models.User.Role;
import com.incture.lms.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up test user data
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(Role.STUDENT);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> user = userService.getUserById(1L);

        assertTrue(user.isPresent());
        assertEquals(testUser, user.get());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> user = userService.getUserById(1L);

        assertFalse(user.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);

        User savedUser = userService.saveUser(testUser);

        assertNotNull(savedUser);
        assertEquals(testUser, savedUser);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testUpdateUser_UserFound() {
        User updatedDetails = new User();
        updatedDetails.setUsername("updatedUser");
        updatedDetails.setEmail("updated@example.com");
        updatedDetails.setPassword("newpassword");
        updatedDetails.setRole(Role.INSTRUCTOR);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        Optional<User> updatedUser = userService.updateUser(1L, updatedDetails);

        assertTrue(updatedUser.isPresent());
        assertEquals("updatedUser", updatedUser.get().getUsername());
        assertEquals("updated@example.com", updatedUser.get().getEmail());
        assertEquals("newpassword", updatedUser.get().getPassword());
        assertEquals(Role.INSTRUCTOR, updatedUser.get().getRole());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        User updatedDetails = new User();
        updatedDetails.setUsername("updatedUser");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> updatedUser = userService.updateUser(1L, updatedDetails);

        assertFalse(updatedUser.isPresent());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
