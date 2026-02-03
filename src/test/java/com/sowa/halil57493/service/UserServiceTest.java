package com.sowa.halil57493.service;

import com.sowa.halil57493.model.User;
import com.sowa.halil57493.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldHashPasswordAndSaveUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("rawPassword");

        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.createUser(user);

        assertNotNull(savedUser);
        assertEquals("hashedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_WhenRepositoryThrowsException_ShouldPropagateException() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("rawPassword");

        when(passwordEncoder.encode("rawPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> userService.createUser(user));
    }

    @Test
    void authenticate_WithCorrectCredentials_ShouldReturnUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPassword", "hashedPassword")).thenReturn(true);

        User authenticatedUser = userService.authenticate("testuser", "rawPassword");

        assertNotNull(authenticatedUser);
        assertEquals("testuser", authenticatedUser.getUsername());
    }

    @Test
    void authenticate_WithIncorrectCredentials_ShouldReturnNull() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        User authenticatedUser = userService.authenticate("testuser", "wrongPassword");

        assertNull(authenticatedUser);
    }

    @Test
    void authenticate_WithNonExistentUser_ShouldReturnNull() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        User authenticatedUser = userService.authenticate("nonexistent", "password");

        assertNull(authenticatedUser);
    }
}
