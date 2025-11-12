package com.example.backend.service;

import com.example.backend.dto.request.EquipmentRequest;
import com.example.backend.dto.response.EquipmentResponse;
import com.example.backend.enums.StatusEquiment;

import java.util.List;

public interface EquipmentService {
    List<EquipmentResponse> getAllEquipments();
    EquipmentResponse findEquipmentById(Integer id);
    EquipmentResponse createEquipment(EquipmentRequest equipmentRequest);
    EquipmentResponse updateEquipment(Integer id, EquipmentRequest equipmentRequest);
    void deleteEquipment(Integer id);

    List<EquipmentResponse> searchEquipments(String type, String brand, StatusEquiment status);
}
