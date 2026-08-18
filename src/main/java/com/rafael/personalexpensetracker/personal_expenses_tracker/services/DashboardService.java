package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.DashboardResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {
    private final AuthenticatedUserService authenticatedUserService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final TransferService transferService;
    private final SavingsBoxService savingsBoxService;
    private final RoutineTaskService routineTaskService;

    public DashboardService(AuthenticatedUserService authenticatedUserService,
                            ExpenseService expenseService,
                            IncomeService incomeService,
                            TransferService transferService,
                            SavingsBoxService savingsBoxService,
                            RoutineTaskService routineTaskService) {
        this.authenticatedUserService = authenticatedUserService;
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.transferService = transferService;
        this.savingsBoxService = savingsBoxService;
        this.routineTaskService = routineTaskService;
    }

    @Transactional
    public DashboardResponseDto get(String email, LocalDate date) {
        UserEntity user = authenticatedUserService.require(email);
        return new DashboardResponseDto(
                savingsBoxService.getBalance(user),
                expenseService.findByUser(user.getUserId()),
                incomeService.findByUser(user.getUserId()),
                transferService.findByUser(user.getUserId()),
                savingsBoxService.findByUser(user.getUserId()),
                routineTaskService.checklist(user.getUserId(), date)
        );
    }
}
