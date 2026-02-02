package com.sowa.halil57493.controller.auth;

import com.sowa.halil57493.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        com.sowa.halil57493.model.User user = new com.sowa.halil57493.model.User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        user.setRole(com.sowa.halil57493.model.Role.USER);
        userRepository.save(user);
    }

    @Test
    void register_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/register")
                .param("username", "newuser")
                .param("email", "new@example.com")
                .param("password", "Password123!")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/login?registered")));
    }

    @Test
    void login_WithValidCredentials_ShouldRedirectToNotes() throws Exception {
        // Use the user created in setUp
        mockMvc.perform(post("/login")
                .param("username", "testuser")
                .param("password", "Password123!")
                .with(csrf()))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/notes")));
    }

    @Test
    void accessNotes_WithoutLogin_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/notes"))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/login")));
    }

    @Test
    @WithMockUser(username = "testuser")
    void accessNotes_WithLogin_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(view().name("notes"));
    }
}
