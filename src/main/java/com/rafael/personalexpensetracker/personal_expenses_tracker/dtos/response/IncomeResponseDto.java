package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.BalanceSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IncomeResponseDto(
        Long id,
        String description,
        BigDecimal amount,
        LocalDateTime date,
        BalanceSource destination,
        Long userId,
        Long savingsBoxId,
        String savingsBoxName
) {
}
