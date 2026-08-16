package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record RoutineChecklistItemResponseDto(
        Long taskId, String title, LocalDate date, boolean completed, OffsetDateTime completedAt
) {}
