package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record SavingsBoxRequestDto(
        @NotBlank(message = "O nome da caixinha não pode estar vazio.")
        String name,

        @NotNull(message = "O ID do usuário é obrigatório.")
        Long userId,

        @NotNull(message = "O saldo inicial é obrigatório.")
        @PositiveOrZero(message = "O saldo inicial não pode ser negativo.")
        BigDecimal initialBalance
) {
}
