package com.example.backend.dto.request;

import com.example.backend.enums.StatusLoan;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer userId;

    @NotNull(message = "El ID del equipo es obligatorio")
    private Integer equipmentId;

    private LocalDateTime expectedReturnDate;

    private StatusLoan status;
}
