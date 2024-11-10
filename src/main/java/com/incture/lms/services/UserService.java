package com.incture.lms.services;

import com.incture.lms.models.User;
import com.incture.lms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    // Get all users
    public List<User> getAllUsers() {
        logger.info("Retrieving all users from database");
        return userRepository.findAll();
    }

    // Get user by id
    public Optional<User> getUserById(Long id) {
        logger.info("Searching for user with ID: {}", id);
        return userRepository.findById(id);
    }

    // Create new user
    public User saveUser(User user) {
        logger.info("Saving new user with username: {}", user.getUsername());
        return userRepository.save(user);
    }

    // Update user by id
    public Optional<User> updateUser(Long id, User userDetails) {
        logger.info("Updating user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setUsername(userDetails.getUsername());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPassword(userDetails.getPassword());
            existingUser.setRole(userDetails.getRole());
            logger.info("User with ID {} updated successfully", id);
            return Optional.of(userRepository.save(existingUser));
        } else {
            logger.warn("User with ID {} not found for update", id);
            return Optional.empty();
        }
    }

    // Delete user by id
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }
}
