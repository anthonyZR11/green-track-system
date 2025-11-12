package com.example.backend.exception;

public class EquipmentNotAvailableException extends RuntimeException {
    public EquipmentNotAvailableException(String message) {
        super(message);
    }
}
