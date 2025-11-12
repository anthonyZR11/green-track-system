package com.example.backend.repository;

import com.example.backend.entities.Equipment;
import com.example.backend.enums.StatusEquiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    List<Equipment> findByDeletedAtIsNull();
    Optional<Equipment> findByIdAndDeletedAtIsNull(Integer id);
    Optional<Equipment> findByname(String name);
    boolean existsByName(String name);

    @Query("SELECT e FROM Equipment e WHERE " +
            "(:type IS NULL OR e.type = :type) AND " +
            "(:brand IS NULL OR e.brand = :brand) AND " +
            "(:status IS NULL OR e.status = :status)" +
            "AND deletedAt IS NULL")
    List<Equipment> findByFilters(
            @Param("type") String type,
            @Param("brand") String brand,
            @Param("status") StatusEquiment status
    );
}


