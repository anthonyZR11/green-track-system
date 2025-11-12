package com.example.backend.dto.request;

import com.example.backend.enums.StatusEquiment;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EquipmentRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank(message = "La marca es obligatoria")
    @Size(min = 2, max = 50)
    private String brand;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(min = 2, max = 80)
    private String type;

    private StatusEquiment status;
}
