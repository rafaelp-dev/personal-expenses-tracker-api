package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "routine_task_occurrences", uniqueConstraints =
        @UniqueConstraint(name = "uk_routine_task_occurrence_date", columnNames = {"routine_task_id", "occurrence_date"}))
@Getter
@Setter
public class RoutineTaskOccurrenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "routine_task_id", nullable = false)
    private RoutineTaskEntity routineTask;

    @Column(name = "occurrence_date", nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean completed = false;

    private OffsetDateTime completedAt;

    public RoutineTaskOccurrenceEntity() {}

    public RoutineTaskOccurrenceEntity(RoutineTaskEntity routineTask, LocalDate date) {
        this.routineTask = routineTask;
        this.date = date;
    }
}
