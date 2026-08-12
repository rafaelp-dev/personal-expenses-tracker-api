package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto(
        @NotBlank(message = "O nome da categoria não pode estar vazio.")
        @Size(max = 80, message = "O nome da categoria deve ter no máximo 80 caracteres.")
        String name,
        @NotNull(message = "O tipo da categoria é obrigatório.") CategoryType type,
        @NotNull(message = "O ID do usuário é obrigatório.") Long userId
) {}
