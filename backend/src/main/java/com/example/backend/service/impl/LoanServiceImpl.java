package com.example.backend.service.impl;

import com.example.backend.dto.request.LoanRequest;
import com.example.backend.dto.response.LoanResponse;
import com.example.backend.entities.Equipment;
import com.example.backend.entities.Loan;
import com.example.backend.entities.User;
import com.example.backend.enums.StatusEquiment;
import com.example.backend.enums.StatusLoan;
import com.example.backend.exception.EquipmentNotAvailableException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.EquipmentRepository;
import com.example.backend.repository.LoanRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    @Override
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LoanResponse findLoanById(Integer id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con id: " + id));
        return convertToResponse(loan);
    }

    @Override
    public LoanResponse createLoan(LoanRequest loanRequest) {
        // Buscar usuario
        User user = userRepository.findById(loanRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + loanRequest.getUserId()));


        System.out.println(user);
        // Buscar equipo
        Equipment equipment = equipmentRepository.findById(loanRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con id: " + loanRequest.getEquipmentId()));

        boolean isEquipmentOnLoan = loanRepository.existsByEquipmentIdAndStatus(
                loanRequest.getEquipmentId(),
                StatusLoan.ACTIVO
        );

        if (isEquipmentOnLoan) {
            throw new EquipmentNotAvailableException(
                    "El equipo '" + equipment.getName() + "' ya se encuentra prestado"
            );
        }

        if (loanRequest.getExpectedReturnDate() != null && loanRequest.getExpectedReturnDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de retorno no puede ser menor a la fecha actual");
        }

        equipment.setStatus(StatusEquiment.PRESTADO);
        equipmentRepository.save(equipment);

        // Si status es null, asignar ACTIVO por defecto
        StatusLoan status = loanRequest.getStatus() != null
                ? loanRequest.getStatus()
                : StatusLoan.ACTIVO;

        Loan loan = Loan.builder()
                .user(user)
                .equipment(equipment)
                .expectedReturnDate(loanRequest.getExpectedReturnDate())
                .status(status)
                .build();


        Loan savedLoan = loanRepository.save(loan);
        return convertToResponse(savedLoan);
    }

    @Override
    public LoanResponse updateLoan(Integer id, LoanRequest loanRequest) {
        // Buscar préstamo existente
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con id: " + id));

        // Buscar usuario (solo si cambió)
        User user = !existingLoan.getUser().getId().equals(loanRequest.getUserId())
                ? userRepository.findById(loanRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + loanRequest.getUserId()))
                : existingLoan.getUser();

        // Buscar equipo (solo si cambió)
        Equipment equipment = !existingLoan.getEquipment().getId().equals(loanRequest.getEquipmentId())
                ? equipmentRepository.findById(loanRequest.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con id: " + loanRequest.getEquipmentId()))
                : existingLoan.getEquipment();

        if (loanRequest.getExpectedReturnDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de retorno no puede ser menor a la fecha actual");
        }

        Loan loan = Loan.builder()
                .id(existingLoan.getId())
                .user(user)
                .equipment(equipment)
                .loanDate(existingLoan.getLoanDate())
                .expectedReturnDate(loanRequest.getExpectedReturnDate())
                .returnDate(existingLoan.getReturnDate())
                .status(loanRequest.getStatus() != null
                        ? loanRequest.getStatus()
                        : existingLoan.getStatus())
                .build();

        Loan updatedLoan = loanRepository.save(loan);
        return convertToResponse(updatedLoan);
    }

    @Override
    public List<LoanResponse> searchLoans(Integer userId, LocalDateTime startDate, LocalDateTime endDate) {
        return loanRepository.findByFilters(userId, startDate, endDate).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LoanResponse returnLoan(Integer id) {
        // Buscar el préstamo
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con id: " + id));

        // Validar que no esté ya devuelto
        if (loan.getStatus() == StatusLoan.DEVUELTO) {
            throw new IllegalStateException("El préstamo ya fue devuelto anteriormente");
        }

        Equipment equipment = loan.getEquipment();
        equipment.setStatus(StatusEquiment.DISPONIBLE);
        equipmentRepository.save(equipment);

        loan.setReturnDate(LocalDateTime.now());
        loan.setStatus(StatusLoan.DEVUELTO);

        Loan updatedLoan = loanRepository.save(loan);
        return convertToResponse(updatedLoan);
    }

    private LoanResponse convertToResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .userId(loan.getUser().getId())
                .username(loan.getUser().getUsername())
                .equipmentId(loan.getEquipment().getId())
                .equipmentName(loan.getEquipment().getName())
                .loanDate(loan.getLoanDate())
                .expectedReturnDate(loan.getExpectedReturnDate())
                .status(loan.getStatus())
                .updatedAt(loan.getUpdatedAt())
                .build();
    }
}
