package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.ExpenseResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.ExpenseEntity;
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
        List<ExpenseResponseDto> expenseResponseDto = expenseService.getAllExpenses();

        return ResponseEntity.ok().body(expenseResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseEntity> getExpenseById(@PathVariable Long id){
        ExpenseEntity expenseEntity = expenseService.getExpenseById(id);

        return ResponseEntity.ok().body(expenseEntity);
    }

    @PostMapping
    public ResponseEntity<ExpenseEntity> createExpense(@Valid @RequestBody ExpenseEntity expense){
        ExpenseEntity expenseEntity = expenseService.createExpense(expense);

        return ResponseEntity.status(HttpStatus.CREATED).body(expenseEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable Long id){
        expenseService.deleteExpenseById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseEntity expense){
        ExpenseEntity expenseEntity = expenseService.updateExpense(id, expense);

        return ResponseEntity.ok().body(expenseEntity);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<ExpenseEntity>> findExpensesByUserId(@PathVariable Long id){
        List<ExpenseEntity> expenseEntityList = expenseService.findByUserId(id);

        return ResponseEntity.ok().body(expenseEntityList);
    }
}
