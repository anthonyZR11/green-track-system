package com.example.backend.entities;

import com.example.backend.enums.Role;
import com.example.backend.enums.StatusEquiment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "equipments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(min = 2, max = 50)
    @Column(nullable = false, unique = false, length = 50)
    private String type;

    @NotBlank(message = "La marca es obligatorio")
    @Size(min = 2, max = 50)
    @Column(nullable = false, unique = false, length = 50)
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        columnDefinition = "ENUM('DISPONIBLE', 'PRESTADO', 'MANTENIMIENTO') DEFAULT 'DISPONIBLE'"
    )
    private StatusEquiment status;

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
        nullable = true,
        columnDefinition = "TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(
        name = "deletedAt",
        nullable = true,
        columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private LocalDateTime deletedAt;
}
