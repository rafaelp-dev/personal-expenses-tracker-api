package com.rafael.personalexpensetracker.personal_expenses_tracker;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.*;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.BalanceResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.SavingsBoxResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.UserResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.BalanceSource;
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

    @Test
    void shouldCreditAndDebitMainBalanceAndRefundDeletedExpense() {
        Long userId = createUser("main@example.com").id();
        incomeService.create(new IncomeRequestDto(
                "Salário", new BigDecimal("3000.00"), userId, BalanceSource.MAIN, null
        ));

        var expense = expenseService.createExpense(new ExpenseRequestDto(
                "Aluguel", "Moradia", new BigDecimal("1200.00"), userId, BalanceSource.MAIN, null
        ));
        assertEquals(new BigDecimal("1800.00"), savingsBoxService.getBalance(userId).mainBalance());

        expenseService.deleteExpenseById(expense.expenseId());
        assertEquals(new BigDecimal("3000.00"), savingsBoxService.getBalance(userId).mainBalance());
    }

    @Test
    void shouldCreditAndDebitSavingsBox() {
        Long userId = createUser("box@example.com").id();
        SavingsBoxResponseDto box = savingsBoxService.create(new SavingsBoxRequestDto(
                "Viagem", userId, new BigDecimal("500.00")
        ));
        incomeService.create(new IncomeRequestDto(
                "Extra", new BigDecimal("200.00"), userId, BalanceSource.SAVINGS_BOX, box.id()
        ));
        expenseService.createExpense(new ExpenseRequestDto(
                "Passagem", "Viagem", new BigDecimal("250.00"), userId,
                BalanceSource.SAVINGS_BOX, box.id()
        ));

        BalanceResponseDto balance = savingsBoxService.getBalance(userId);
        assertEquals(new BigDecimal("450.00"), balance.savingsBoxesBalance());
        assertEquals(new BigDecimal("450.00"), balance.totalBalance());
    }

    @Test
    void shouldRejectExpenseWithoutEnoughFunds() {
        Long userId = createUser("empty@example.com").id();

        assertThrows(ResponseStatusException.class, () -> expenseService.createExpense(new ExpenseRequestDto(
                "Compra", "Outros", new BigDecimal("10.00"), userId, BalanceSource.MAIN, null
        )));
    }

    private UserResponseDto createUser(String email) {
        return userService.createUser(new UserRequestDto("Test", email, "test-password"));
    }
}
