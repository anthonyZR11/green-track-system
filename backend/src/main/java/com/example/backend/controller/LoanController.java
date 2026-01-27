package com.example.backend.controller;

import com.example.backend.dto.request.LoanRequest;
import com.example.backend.dto.response.LoanResponse;
import com.example.backend.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
@Tag(name = "4 - Préstamos", description = "Gestión de préstamos de equipos")
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/search")
    @Operation(summary = "Buscar préstamos con filtros")
    public ResponseEntity<List<LoanResponse>> searchLoans(
            @Parameter(description = "ID del usuario") @RequestParam(required = false) Integer userId,
            @Parameter(description = "Fecha de inicio") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Fecha de fin") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<LoanResponse> loans = loanService.searchLoans(userId, startDate, endDate);
        return ResponseEntity.ok(loans);
    }

    @GetMapping
    @Operation(summary = "Listar todos los préstamos")
    public ResponseEntity<List<LoanResponse>> getAllLoans() {
        List<LoanResponse> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener préstamo por ID")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Integer id) {
        LoanResponse loan = loanService.findLoanById(id);
        return ResponseEntity.ok(loan);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo préstamo")
    public ResponseEntity<LoanResponse> createLoan(@Valid @RequestBody LoanRequest loanRequest) {
        LoanResponse response = loanService.createLoan(loanRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar préstamo")
    public ResponseEntity<LoanResponse> updateLoan(
            @PathVariable Integer id,
            @Valid @RequestBody LoanRequest loanRequest) {
        LoanResponse response = loanService.updateLoan(id, loanRequest);
        return ResponseEntity.ok(response);
    }

    // ✅ Endpoint para devolver equipo
    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Devolver equipo prestado", description = "Marca el préstamo como devuelto y registra la fecha de devolución")
    public ResponseEntity<LoanResponse> returnLoan(@PathVariable Integer id) {
        LoanResponse response = loanService.returnLoan(id);
        return ResponseEntity.ok(response);
    }
}
