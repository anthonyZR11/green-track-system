package com.example.backend.dto.response;

import com.example.backend.enums.Role;
import com.example.backend.enums.StatusEquiment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EquipmentResponse {
    private Integer id;
    private String name;
    private String type;
    private String brand;
    private StatusEquiment status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
