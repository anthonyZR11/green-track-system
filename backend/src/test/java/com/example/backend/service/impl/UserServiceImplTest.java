import com.example.backend.dto.response.UserResponse;
import com.example.backend.entities.User;
import com.example.backend.enums.Role;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .name("Juan Pérez")
                .username("juanp")
                .email("juan@ejemplo.com")
                .password("hashedpassword")
                .role(Role.USER)
                .createdAt(LocalDateTime.of(2025, 11, 1, 10, 30))
                .updatedAt(LocalDateTime.of(2025, 11, 12, 7, 0))
                .build();
    }

    @Test
    void shouldReturnUserResponseWhenUserExists() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Act
        UserResponse response = userService.findUserById(1);

        // Assert
        assertNotNull(response);
        assertEquals("Juan Pérez", response.getName());
        assertEquals("juanp", response.getUsername());
        assertEquals(Role.USER, response.getRole());
        verify(userRepository, times(1)).findById(1);
    }
}
