package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import java.math.BigDecimal;
import java.util.List;

public record BalanceResponseDto(
        Long userId,
        BigDecimal mainBalance,
        BigDecimal savingsBoxesBalance,
        BigDecimal totalBalance,
        List<SavingsBoxResponseDto> savingsBoxes
) {
}
