package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.ExpenseEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<List<ExpenseEntity>> getAllExpenses(){
        List<ExpenseEntity> expenses = expenseService.getAllExpenses();

        return ResponseEntity.ok().body(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseEntity> getExpenseById(@PathVariable Long id){
        return expenseService.getExpenseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExpenseEntity> createExpense(@Valid @RequestBody ExpenseEntity expense){
        ExpenseEntity expenseEntity = expenseService.createExpense(expense);

        return ResponseEntity.status(HttpStatus.CREATED).body(expenseEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenseById(@PathVariable Long id){
        if (expenseService.getExpenseById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }

        expenseService.deleteExpenseById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseEntity expense){
        try {
            ExpenseEntity expenseEntity = expenseService.updateExpense(id, expense);
            return ResponseEntity.ok().body(expenseEntity);
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
