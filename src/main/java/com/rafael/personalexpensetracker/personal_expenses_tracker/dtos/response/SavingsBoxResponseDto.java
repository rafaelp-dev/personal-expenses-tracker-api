package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import java.math.BigDecimal;

public record SavingsBoxResponseDto(
        Long id,
        String name,
        BigDecimal balance,
        Long userId
) {
}
