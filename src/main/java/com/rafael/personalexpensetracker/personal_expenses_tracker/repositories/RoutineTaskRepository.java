package com.rafael.personalexpensetracker.personal_expenses_tracker.repositories;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.RoutineTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoutineTaskRepository extends JpaRepository<RoutineTaskEntity, Long> {
    List<RoutineTaskEntity> findByUser_UserIdAndActiveTrueOrderByTitleAsc(Long userId);
    List<RoutineTaskEntity> findByUser_UserIdOrderByTitleAsc(Long userId);
    Optional<RoutineTaskEntity> findByIdAndUser_UserId(Long id, Long userId);
}
