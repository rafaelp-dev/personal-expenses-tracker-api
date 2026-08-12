package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.ExpenseRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.ExpenseResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.BalanceSource;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.ExpenseEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.SavingsBoxEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.ExpenseRepository;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final BalanceService balanceService;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository,
                          BalanceService balanceService) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.balanceService = balanceService;
    }

    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ExpenseResponseDto getExpenseById(Long id) {
        return toResponse(findExpense(id));
    }

    @Transactional
    public ExpenseResponseDto createExpense(ExpenseRequestDto request) {
        UserEntity user = findUser(request.userId());
        BalanceSource source = request.source() == null ? BalanceSource.MAIN : request.source();
        SavingsBoxEntity box = balanceService.debit(user, source, request.savingsBoxId(), request.price());

        return toResponse(expenseRepository.save(new ExpenseEntity(
                request.name(), request.category(), request.price(), user, source, box
        )));
    }

    @Transactional
    public void deleteExpenseById(Long id) {
        ExpenseEntity expense = findExpense(id);
        balanceService.credit(
                expense.getUser(), expense.getSource(),
                expense.getSavingsBox() == null ? null : expense.getSavingsBox().getId(), expense.getPrice()
        );
        expenseRepository.delete(expense);
    }

    @Transactional
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto request) {
        ExpenseEntity expense = findExpense(id);

        balanceService.credit(
                expense.getUser(), expense.getSource(),
                expense.getSavingsBox() == null ? null : expense.getSavingsBox().getId(), expense.getPrice()
        );

        UserEntity user = request.userId() == null ? expense.getUser() : findUser(request.userId());
        BigDecimal price = request.price() == null ? expense.getPrice() : request.price();
        BalanceSource source = request.source() == null ? expense.getSource() : request.source();
        Long boxId = resolveBoxId(request, expense, user, source);
        SavingsBoxEntity box = balanceService.debit(user, source, boxId, price);

        if (request.name() != null) expense.setName(request.name());
        if (request.category() != null) expense.setCategory(request.category());
        expense.setPrice(price);
        expense.setUser(user);
        expense.setSource(source);
        expense.setSavingsBox(box);

        return toResponse(expenseRepository.save(expense));
    }

    public List<ExpenseResponseDto> findByUserId(Long id) {
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

    private ExpenseEntity findExpense(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Gasto com ID " + id + " não encontrado."
        ));
    }

    private UserEntity findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuário com ID " + id + " não encontrado."
        ));
    }

    private ExpenseResponseDto toResponse(ExpenseEntity expense) {
        SavingsBoxEntity box = expense.getSavingsBox();
        return new ExpenseResponseDto(
                expense.getExpenseId(), expense.getName(), expense.getCategory(), expense.getPrice(),
                expense.getDate(), expense.getUser().getName(), expense.getSource(),
                box == null ? null : box.getId(), box == null ? null : box.getName()
        );
    }
}
