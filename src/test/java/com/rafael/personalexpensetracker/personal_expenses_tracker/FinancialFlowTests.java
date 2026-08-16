package com.rafael.personalexpensetracker.personal_expenses_tracker;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.*;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.BalanceResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.SavingsBoxResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.UserResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.BalanceSource;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryType;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.CategoryService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.ExpenseService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.IncomeService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.SavingsBoxService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:financial-flow-test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "app.auth.jwt-secret=test-secret-with-at-least-32-characters"
})
@Transactional
class FinancialFlowTests {
    @Autowired UserService userService;
    @Autowired IncomeService incomeService;
    @Autowired SavingsBoxService savingsBoxService;
    @Autowired ExpenseService expenseService;
    @Autowired CategoryService categoryService;

    @Test
    void shouldCreditAndDebitMainBalanceAndRefundDeletedExpense() {
        String email = "main@example.com";
        createUser(email);
        Long incomeCategoryId = createCategory("Salário", CategoryType.INCOME, email);
        Long expenseCategoryId = createCategory("Moradia", CategoryType.EXPENSE, email);
        incomeService.create(new IncomeRequestDto(
                "Salário", new BigDecimal("3000.00"), incomeCategoryId, BalanceSource.MAIN, null
        ), email);

        var expense = expenseService.createExpense(new ExpenseRequestDto(
                "Aluguel", expenseCategoryId, new BigDecimal("1200.00"), BalanceSource.MAIN, null
        ), email);
        assertEquals(new BigDecimal("1800.00"), savingsBoxService.getBalance(email).mainBalance());

        expenseService.deleteExpenseById(expense.expenseId(), email);
        assertEquals(new BigDecimal("3000.00"), savingsBoxService.getBalance(email).mainBalance());
    }

    @Test
    void shouldCreditAndDebitSavingsBox() {
        String email = "box@example.com";
        createUser(email);
        Long incomeCategoryId = createCategory("Extra", CategoryType.INCOME, email);
        Long expenseCategoryId = createCategory("Viagem", CategoryType.EXPENSE, email);
        SavingsBoxResponseDto box = savingsBoxService.create(new SavingsBoxRequestDto(
                "Viagem", new BigDecimal("500.00")
        ), email);
        incomeService.create(new IncomeRequestDto(
                "Extra", new BigDecimal("200.00"), incomeCategoryId, BalanceSource.SAVINGS_BOX, box.id()
        ), email);
        expenseService.createExpense(new ExpenseRequestDto(
                "Passagem", expenseCategoryId, new BigDecimal("250.00"),
                BalanceSource.SAVINGS_BOX, box.id()
        ), email);

        BalanceResponseDto balance = savingsBoxService.getBalance(email);
        assertEquals(new BigDecimal("450.00"), balance.savingsBoxesBalance());
        assertEquals(new BigDecimal("450.00"), balance.totalBalance());
    }

    @Test
    void shouldRejectExpenseWithoutEnoughFunds() {
        String email = "empty@example.com";
        createUser(email);
        Long expenseCategoryId = createCategory("Outros", CategoryType.EXPENSE, email);

        assertThrows(ResponseStatusException.class, () -> expenseService.createExpense(new ExpenseRequestDto(
                "Compra", expenseCategoryId, new BigDecimal("10.00"), BalanceSource.MAIN, null
        ), email));
    }

    private UserResponseDto createUser(String email) {
        return userService.createUser(new UserRequestDto("Test", email, "test-password"));
    }

    private Long createCategory(String name, CategoryType type, String email) {
        return categoryService.create(new CategoryRequestDto(name, type), email).id();
    }
}
