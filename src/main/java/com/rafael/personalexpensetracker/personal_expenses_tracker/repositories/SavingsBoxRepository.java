package com.rafael.personalexpensetracker.personal_expenses_tracker.repositories;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.SavingsBoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavingsBoxRepository extends JpaRepository<SavingsBoxEntity, Long> {
    List<SavingsBoxEntity> findByUser_UserId(Long userId);
    Optional<SavingsBoxEntity> findByIdAndUser_UserId(Long id, Long userId);
}
