package com.example.backend.entities;

import com.example.backend.enums.StatusLoan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipmentId", nullable = false)
    private Equipment equipment;

    @PastOrPresent(message = "La fecha del préstamo no puede ser futura")
    @CreationTimestamp
    @Column(nullable = false,
        updatable = false,
        name = "loanDate")
    private LocalDateTime loanDate;

    // ✅ Fecha esperada de devolución (cuándo DEBERÍA devolverse)
    @Column(name = "expectedReturnDate")
    private LocalDateTime expectedReturnDate;

    // ✅ Fecha real de devolución (cuándo se devolvió realmente)
    @Column(name = "returnDate")
    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            columnDefinition = "ENUM('ACTIVO', 'ATRASADO', 'DEVUELTO') DEFAULT 'ACTIVO'"
    )
    private StatusLoan status;

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
