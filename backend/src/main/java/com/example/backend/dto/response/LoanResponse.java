package com.example.backend.dto.response;

import com.example.backend.enums.StatusLoan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse {
    private Integer id;
    private Integer userId;
    private String username;
    private Integer equipmentId;
    private String equipmentName;
    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime returnDate;
    private StatusLoan status;
    private LocalDateTime updatedAt;
}
