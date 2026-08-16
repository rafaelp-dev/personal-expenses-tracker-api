package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.ExpenseRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.ExpenseResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.BalanceSource;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryType;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.ExpenseEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.SavingsBoxEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final BalanceService balanceService;
    private final CategoryService categoryService;

    public ExpenseService(ExpenseRepository expenseRepository, AuthenticatedUserService authenticatedUserService,
                          BalanceService balanceService, CategoryService categoryService) {
        this.expenseRepository = expenseRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.balanceService = balanceService;
        this.categoryService = categoryService;
    }

    public List<ExpenseResponseDto> getAllExpenses(String email) {
        return findByUserId(authenticatedUserService.require(email).getUserId());
    }

    public ExpenseResponseDto getExpenseById(Long id, String email) {
        return toResponse(findExpense(id, authenticatedUserService.require(email).getUserId()));
    }

    @Transactional
    public ExpenseResponseDto createExpense(ExpenseRequestDto request, String email) {
        UserEntity user = authenticatedUserService.require(email);
        CategoryEntity category = categoryService.findForUserAndType(
                request.categoryId(), user.getUserId(), CategoryType.EXPENSE);
        BalanceSource source = request.source() == null ? BalanceSource.MAIN : request.source();
        SavingsBoxEntity box = balanceService.debit(user, source, request.savingsBoxId(), request.price());

        return toResponse(expenseRepository.save(new ExpenseEntity(
                request.name(), category, request.price(), user, source, box
        )));
    }

    @Transactional
    public void deleteExpenseById(Long id, String email) {
        ExpenseEntity expense = findExpense(id, authenticatedUserService.require(email).getUserId());
        balanceService.credit(
                expense.getUser(), expense.getSource(),
                expense.getSavingsBox() == null ? null : expense.getSavingsBox().getId(), expense.getPrice()
        );
        expenseRepository.delete(expense);
    }

    @Transactional
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto request, String email) {
        UserEntity user = authenticatedUserService.require(email);
        ExpenseEntity expense = findExpense(id, user.getUserId());

        balanceService.credit(
                expense.getUser(), expense.getSource(),
                expense.getSavingsBox() == null ? null : expense.getSavingsBox().getId(), expense.getPrice()
        );

        Long categoryId = request.categoryId() == null
                ? expense.getCategoryEntity().getId()
                : request.categoryId();
        CategoryEntity category = categoryService.findForUserAndType(
                categoryId, user.getUserId(), CategoryType.EXPENSE);
        BigDecimal price = request.price() == null ? expense.getPrice() : request.price();
        BalanceSource source = request.source() == null ? expense.getSource() : request.source();
        Long boxId = resolveBoxId(request, expense, user, source);
        SavingsBoxEntity box = balanceService.debit(user, source, boxId, price);

        if (request.name() != null) expense.setName(request.name());
        expense.setCategoryEntity(category);
        expense.setCategory(category.getName());
        expense.setPrice(price);
        expense.setUser(user);
        expense.setSource(source);
        expense.setSavingsBox(box);

        return toResponse(expenseRepository.save(expense));
    }

    private List<ExpenseResponseDto> findByUserId(Long id) {
        return expenseRepository.findByUser_UserId(id).stream().map(this::toResponse).toList();
    }

    private Long resolveBoxId(ExpenseRequestDto request, ExpenseEntity expense,
                              UserEntity user, BalanceSource source) {
        if (source == BalanceSource.MAIN) return null;
        if (request.savingsBoxId() != null) return request.savingsBoxId();
        if (expense.getSavingsBox() != null && expense.getUser().getUserId().equals(user.getUserId())) {
            return expense.getSavingsBox().getId();
        }
        return null;
    }

    private ExpenseEntity findExpense(Long id, Long userId) {
        return expenseRepository.findByExpenseIdAndUser_UserId(id, userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Gasto com ID " + id + " não encontrado."
        ));
    }

    private ExpenseResponseDto toResponse(ExpenseEntity expense) {
        SavingsBoxEntity box = expense.getSavingsBox();
        CategoryEntity category = expense.getCategoryEntity();
        return new ExpenseResponseDto(
                expense.getExpenseId(), expense.getName(), category == null ? null : category.getId(),
                category == null ? expense.getCategory() : category.getName(), expense.getPrice(),
                expense.getDate(), expense.getUser().getName(), expense.getSource(),
                box == null ? null : box.getId(), box == null ? null : box.getName()
        );
    }
}
