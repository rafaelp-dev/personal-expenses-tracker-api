package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RoutineTaskCompletionRequestDto(
        @NotNull(message = "A data é obrigatória.") LocalDate date,
        @NotNull(message = "O estado de conclusão é obrigatório.") Boolean completed
) {}
