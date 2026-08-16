package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.IncomeRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.IncomeResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.IncomeEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryType;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.SavingsBoxEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.IncomeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final BalanceService balanceService;
    private final CategoryService categoryService;

    public IncomeService(IncomeRepository incomeRepository, AuthenticatedUserService authenticatedUserService,
                         BalanceService balanceService, CategoryService categoryService) {
        this.incomeRepository = incomeRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.balanceService = balanceService;
        this.categoryService = categoryService;
    }

    @Transactional
    public IncomeResponseDto create(IncomeRequestDto request, String email) {
        UserEntity user = authenticatedUserService.require(email);
        SavingsBoxEntity box = balanceService.credit(
                user, request.destination(), request.savingsBoxId(), request.amount()
        );
        var category = categoryService.findForUserAndType(
                request.categoryId(), user.getUserId(), CategoryType.INCOME);
        return toResponse(incomeRepository.save(new IncomeEntity(
                request.description(), request.amount(), category, request.destination(), user, box
        )));
    }

    public List<IncomeResponseDto> findByUser(String email) {
        Long userId = authenticatedUserService.require(email).getUserId();
        return incomeRepository.findByUser_UserId(userId).stream().map(this::toResponse).toList();
    }

    private IncomeResponseDto toResponse(IncomeEntity income) {
        SavingsBoxEntity box = income.getSavingsBox();
        var category = income.getCategory();
        return new IncomeResponseDto(
                income.getId(), income.getDescription(), category == null ? null : category.getId(),
                category == null ? null : category.getName(), income.getAmount(), income.getDate(),
                income.getDestination(), income.getUser().getUserId(),
                box == null ? null : box.getId(), box == null ? null : box.getName()
        );
    }
}
