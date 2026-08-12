package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.BalanceSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record IncomeRequestDto(
        @NotBlank(message = "A descrição da receita não pode estar vazia.")
        String description,

        @NotNull(message = "O valor da receita é obrigatório.")
        @Positive(message = "O valor da receita deve ser maior que zero.")
        BigDecimal amount,

        @NotNull(message = "O ID do usuário é obrigatório.")
        Long userId,

        @NotNull(message = "O destino da receita é obrigatório.")
        BalanceSource destination,

        Long savingsBoxId
) {
}
