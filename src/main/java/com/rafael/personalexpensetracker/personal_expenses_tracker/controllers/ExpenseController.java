package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.ExpenseEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
