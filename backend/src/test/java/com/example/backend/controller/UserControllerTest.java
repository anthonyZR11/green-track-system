package com.example.backend.controller;

import com.example.backend.dto.response.UserResponse;
import com.example.backend.enums.Role;
import com.example.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userResponse = new UserResponse(
                1,
                "Juan Pérez",
                "juanp",
                "juan@ejemplo.com",
                Role.USER,
                LocalDateTime.of(2025, 11, 1, 10, 30),
                LocalDateTime.of(2025, 11, 12, 7, 0)
        );
    }

    @Test
    void shouldSearchUserById() throws Exception {
        // Arrange
        Integer userId = 1;
        when(userService.findUserById(userId)).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.username").value("juanp"))
                .andExpect(jsonPath("$.email").value("juan@ejemplo.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());

        // Verify
        verify(userService, times(1)).findUserById(userId);
    }
}
