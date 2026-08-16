package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public record RoutineTaskResponseDto(
        Long id, String title, Set<DayOfWeek> weekdays, LocalDate startsOn, boolean active, Long userId
) {}
