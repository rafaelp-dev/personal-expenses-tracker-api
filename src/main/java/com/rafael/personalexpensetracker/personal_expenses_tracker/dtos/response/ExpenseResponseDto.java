package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.BalanceSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResponseDto(
        Long expenseId,
        String name,
        Long categoryId,
        String categoryName,
        BigDecimal price,
        LocalDateTime date,
        String userName,
        BalanceSource source,
        Long savingsBoxId,
        String savingsBoxName
) {
}
