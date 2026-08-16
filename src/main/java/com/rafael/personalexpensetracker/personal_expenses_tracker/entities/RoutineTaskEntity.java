package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "routine_tasks")
@Getter
@Setter
public class RoutineTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "routine_task_weekdays", joinColumns = @JoinColumn(name = "routine_task_id"))
    @Column(name = "weekday", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> weekdays = new HashSet<>();

    @Column(nullable = false)
    private LocalDate startsOn;

    @Column(nullable = false)
    private boolean active = true;

    private LocalDate endsOn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public RoutineTaskEntity() {}

    public RoutineTaskEntity(String title, Set<DayOfWeek> weekdays, LocalDate startsOn, UserEntity user) {
        this.title = title;
        this.weekdays = new HashSet<>(weekdays);
        this.startsOn = startsOn;
        this.user = user;
    }
}
