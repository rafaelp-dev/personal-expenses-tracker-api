package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import java.util.List;

public record DashboardResponseDto(
        BalanceResponseDto balance,
        List<ExpenseResponseDto> expenses,
        List<IncomeResponseDto> incomes,
        List<TransferResponseDto> transfers,
        List<SavingsBoxResponseDto> boxes,
        List<RoutineChecklistItemResponseDto> checklist
) {}
