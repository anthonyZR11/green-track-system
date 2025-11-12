package com.example.backend.repository;

import com.example.backend.entities.Loan;
import com.example.backend.enums.StatusLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
    @Query("SELECT l FROM Loan l WHERE " +
            "(:userId IS NULL OR l.user.id = :userId) AND " +
            "(:startDate IS NULL OR l.loanDate >= :startDate) AND " +
            "(:endDate IS NULL OR l.loanDate <= :endDate)")
    List<Loan> findByFilters(
            @Param("userId") Integer userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    boolean existsByEquipmentIdAndStatus(Integer equipmentId, StatusLoan status);
    @Query("SELECT l FROM Loan l WHERE l.equipment.id = :equipmentId AND l.status = :status")
    List<Loan> findByEquipmentIdAndStatus(
            @Param("equipmentId") Integer equipmentId,
            @Param("status") StatusLoan status
    );
}
