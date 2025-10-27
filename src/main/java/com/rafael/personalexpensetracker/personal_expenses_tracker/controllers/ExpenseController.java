package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.ExpenseRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.ExpenseResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses(){
        List<ExpenseResponseDto> expenseResponseDtos = expenseService.getAllExpenses();

        return ResponseEntity.ok().body(expenseResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> getExpenseById(@PathVariable Long id){
        ExpenseResponseDto expenseResponseDto = expenseService.getExpenseById(id);

        return ResponseEntity.ok().body(expenseResponseDto);
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDto> createExpense(@Valid @RequestBody ExpenseRequestDto expenseRequestDto){
        ExpenseResponseDto expenseResponseDto = expenseService.createExpense(expenseRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(expenseResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable Long id){
        expenseService.deleteExpenseById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long id, @RequestBody ExpenseRequestDto expenseRequestDto){
        ExpenseResponseDto expenseResponseDto = expenseService.updateExpense(id, expenseRequestDto);

        return ResponseEntity.ok().body(expenseResponseDto);
    }
}
