package com.example.backend.controller;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.dto.response.ErrorResponse;
import com.example.backend.enums.StatusEquiment;
import com.example.backend.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipments")
@Tag(name = "3. Equipos", description = "API de gestión de equipos")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    @Operation(summary = "Listar equipos", description = "Obtiene la lista de todos los equipos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado - Token inválido o ausente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<EquipmentResponse>> getAllUsers() {
        List<EquipmentResponse> equipments = equipmentService.getAllEquipments();
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponse> findUserById(@PathVariable Integer id) {
        EquipmentResponse user = equipmentService.findEquipmentById(id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EquipmentResponse> createUser(@Valid @RequestBody EquipmentRequest equipmentRequest) {
        EquipmentResponse response = equipmentService.createEquipment(equipmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponse> updateUser(@Valid @PathVariable Integer id, @RequestBody EquipmentRequest equipmentRequest) {
        EquipmentResponse response = equipmentService.updateEquipment(id, equipmentRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EquipmentResponse>> searchEquipments(
            @Parameter(description = "Tipo de equipo", example = "DESKTOP")
            @RequestParam(required = false) String type,

            @Parameter(description = "Marca del equipo", example = "LG")
            @RequestParam(required = false) String brand,

            @Parameter(description = "Estado del equipo")
            @RequestParam(required = false) StatusEquiment status
    ) {
        List<EquipmentResponse> equipments = equipmentService.searchEquipments(type, brand, status);
        return ResponseEntity.ok(equipments);
    }
}
