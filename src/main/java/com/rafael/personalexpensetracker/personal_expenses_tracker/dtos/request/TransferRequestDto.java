package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.TransferDirection;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestDto(
        @NotNull(message = "O valor da transferência é obrigatório.")
        @Positive(message = "O valor da transferência deve ser maior que zero.")
        BigDecimal amount,
        @NotNull(message = "A direção da transferência é obrigatória.")
        TransferDirection direction,
        @NotNull(message = "A caixinha é obrigatória.")
        Long savingsBoxId
) {}
