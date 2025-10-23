package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ExpenseRequestDto(
        @NotBlank(message = "O nome do gasto não pode estar vazio.")
        String name,

        @NotBlank(message = "A categoria não pode estar vazia.")
        String category,

        @NotNull(message = "O preço do gasto não pode estar vazio.")
        @Positive(message = "O preço do gasto deve ser maior do que 0.")
        Integer price,

        @NotNull(message = "O ID de usuário não pode estar vazio.")
        Long userId
) {
}
