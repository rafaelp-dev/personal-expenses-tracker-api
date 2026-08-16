package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public record RoutineTaskRequestDto(
        @NotBlank(message = "O título da tarefa não pode estar vazio.") String title,
        @NotEmpty(message = "Escolha pelo menos um dia da semana.") Set<@NotNull DayOfWeek> weekdays,
        LocalDate startsOn
) {}
