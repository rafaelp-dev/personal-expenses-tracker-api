package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import java.time.LocalDateTime;

public record ExpenseResponseDto(
        Long expenseId,
        String name,
        String category,
        Integer price,
        LocalDateTime date,
        String userName
) {
}
