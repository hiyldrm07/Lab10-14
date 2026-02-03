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

    @Test
    void login_WithoutCsrf_ShouldFail() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "testuser")
                .param("password", "Password123!"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void accessAdmin_WithUserRole_ShouldFail() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void accessAdmin_WithAdminRole_ShouldSucceed() throws Exception {
        // Assuming there is an admin endpoint, if not this might fail 404 but should
        // pass security check (not 403)
        // Adjust depending on if the endpoint exists.
        // Let's check if AdminController exists.
        // Based on file list, yes:
        // main\java\com\sowa\halil57493\controller\AdminController.java
        // Let's assume it maps to /admin/dashboard or similar.
        // If I make a request to /admin/something that doesn't exist, it might be 404,
        // but NOT 403.
        // If I want to verify security, checking for != 403 is one way, or check if it
        // reaches controller.

        // Let's check AdminController content first to be sure of the path.
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk());
    }
}
