package com.example.backend.entities;

import com.example.backend.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name ="users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(min = 3, max = 80)
    @Column(nullable = false, unique = true, length = 80)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            columnDefinition = "ENUM('ADMIN', 'USER') DEFAULT 'ADMIN'"
    )
    private Role role;

    @CreationTimestamp
    @Column(
        name = "createdAt",
        updatable = false,
        nullable = false,
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(
        name = "updatedAt",
        columnDefinition = "TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(
        name = "deletedAt",
        columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private LocalDateTime deletedAt;
}
