package com.rafael.personalexpensetracker.personal_expenses_tracker.repositories;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Long> {
    List<IncomeEntity> findByUser_UserId(Long userId);
}
