package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResponseDto(
        Long expenseId,
        String name,
        String category,
        BigDecimal price,
        LocalDateTime date,
        String userName
) {
}
