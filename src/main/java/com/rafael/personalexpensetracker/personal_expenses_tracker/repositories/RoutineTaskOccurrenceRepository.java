package com.rafael.personalexpensetracker.personal_expenses_tracker.repositories;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.RoutineTaskOccurrenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RoutineTaskOccurrenceRepository extends JpaRepository<RoutineTaskOccurrenceEntity, Long> {
    Optional<RoutineTaskOccurrenceEntity> findByRoutineTask_IdAndDate(Long routineTaskId, LocalDate date);
}
