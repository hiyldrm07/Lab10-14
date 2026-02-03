package com.sowa.halil57493.service;

import com.sowa.halil57493.controller.auth.RegisterRequest;
import com.sowa.halil57493.model.Role;
import com.sowa.halil57493.model.User;
import com.sowa.halil57493.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ShouldSaveUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("Password123!");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setRole(Role.USER);

        when(passwordEncoder.encode(any())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authenticationService.register(request);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        verify(passwordEncoder).encode("Password123!");

        User capturedUser = userCaptor.getValue();
        assertEquals("testuser", capturedUser.getUsername());
        assertEquals("test@example.com", capturedUser.getEmail());
        assertEquals("hashed", capturedUser.getPassword());
        assertEquals(Role.USER, capturedUser.getRole());
    }
}
