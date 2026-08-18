package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.TransferDirection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponseDto(
        Long id, BigDecimal amount, TransferDirection direction, LocalDateTime date,
        Long savingsBoxId, String savingsBoxName
) {}
