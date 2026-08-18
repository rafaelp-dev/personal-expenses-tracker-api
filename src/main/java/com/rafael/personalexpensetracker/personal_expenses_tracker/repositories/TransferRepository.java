package com.rafael.personalexpensetracker.personal_expenses_tracker.repositories;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
    List<TransferEntity> findByUser_UserIdOrderByDateDesc(Long userId);
}
