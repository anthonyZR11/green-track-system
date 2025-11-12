package com.example.backend.service.impl;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.entities.Equipment;
import com.example.backend.enums.StatusEquiment;
import com.example.backend.exception.DuplicateResourceException;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.EquipmentRepository;
import com.example.backend.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public List<EquipmentResponse> getAllEquipments() {
        return equipmentRepository.findByDeletedAtIsNull().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentResponse findEquipmentById(Integer id) {
        Equipment equipment = equipmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con id: " + id));

        return convertToResponse(equipment);
    }

    @Override
    public EquipmentResponse createEquipment(EquipmentRequest equipmentRequest) {
        if (equipmentRepository.existsByName(equipmentRequest.getName())) {
            throw new DuplicateResourceException("El Equipo ya existe");
        }

        System.out.println(equipmentRequest);

        Equipment equipment = Equipment.builder()
                .name(equipmentRequest.getName())
                .brand(equipmentRequest.getBrand())
                .type(equipmentRequest.getType())
                .status(equipmentRequest.getStatus() != null
                        ? equipmentRequest.getStatus()
                        : StatusEquiment.DISPONIBLE)
                .build();

        Equipment saveEquipment = equipmentRepository.save(equipment);

        return convertToResponse(saveEquipment);
    }

    @Override
    public EquipmentResponse updateEquipment(Integer id, EquipmentRequest equipmentRequest) {
        Equipment equipmentData = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con id: " + id));

        if (!equipmentData.getName().equals(equipmentRequest.getName()) &&
                equipmentRepository.existsByName(equipmentRequest.getName())) {
            throw new DuplicateResourceException("El equipo ya existe");
        }

        Equipment equipment = Equipment.builder()
                .id(equipmentData.getId())
                .name(equipmentRequest.getName())
                .type(equipmentRequest.getType())
                .brand(equipmentRequest.getBrand())
                .status(equipmentRequest.getStatus() != null
                        ? equipmentRequest.getStatus()
                        : StatusEquiment.DISPONIBLE)
                .build();

        Equipment saveEquipment = equipmentRepository.save(equipment);

        return convertToResponse(saveEquipment);
    }

    @Override
    public void deleteEquipment(Integer id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo no encontrado con id: " + id));

        equipment.setDeletedAt(LocalDateTime.now());
        equipmentRepository.save(equipment);
    }

    @Override
    public List<EquipmentResponse> searchEquipments(String type, String brand, StatusEquiment status) {
        return equipmentRepository.findByFilters(type, brand, status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private EquipmentResponse convertToResponse(Equipment equipment) {
        return EquipmentResponse.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .type(equipment.getType())
                .brand(equipment.getBrand())
                .status(equipment.getStatus())
                .createdAt(equipment.getCreatedAt())
                .updatedAt(equipment.getUpdatedAt())
                .build();
    }
}
