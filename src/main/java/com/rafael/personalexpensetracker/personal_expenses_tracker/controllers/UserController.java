package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.UserRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.ExpenseResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.UserResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.BalanceResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.IncomeResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.SavingsBoxResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.ExpenseService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.IncomeService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.SavingsBoxService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final SavingsBoxService savingsBoxService;

    public UserController(UserService userService, ExpenseService expenseService,
                          IncomeService incomeService, SavingsBoxService savingsBoxService){
        this.userService = userService;
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.savingsBoxService = savingsBoxService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getAuthenticatedUser(Authentication authentication){
        return ResponseEntity.ok(userService.getAuthenticatedUser(authentication.getName()));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(Authentication authentication){
        userService.deleteAuthenticatedUser(authentication.getName());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto userRequestDto, Authentication authentication) {
        UserResponseDto userResponseDto = userService.updateAuthenticatedUser(authentication.getName(), userRequestDto);

        return ResponseEntity.ok().body(userResponseDto);
    }

    @GetMapping("/me/expenses")
    public ResponseEntity<List<ExpenseResponseDto>> getUserExpenses(Authentication authentication){
        List<ExpenseResponseDto> expenseResponseDtos = expenseService.getAllExpenses(authentication.getName());

        return ResponseEntity.ok().body(expenseResponseDtos);
    }

    @GetMapping("/me/incomes")
    public ResponseEntity<List<IncomeResponseDto>> getUserIncomes(Authentication authentication) {
        return ResponseEntity.ok(incomeService.findByUser(authentication.getName()));
    }

    @GetMapping("/me/savings-boxes")
    public ResponseEntity<List<SavingsBoxResponseDto>> getUserSavingsBoxes(Authentication authentication) {
        return ResponseEntity.ok(savingsBoxService.findByUser(authentication.getName()));
    }

    @GetMapping("/me/balance")
    public ResponseEntity<BalanceResponseDto> getUserBalance(Authentication authentication) {
        return ResponseEntity.ok(savingsBoxService.getBalance(authentication.getName()));
    }
}
