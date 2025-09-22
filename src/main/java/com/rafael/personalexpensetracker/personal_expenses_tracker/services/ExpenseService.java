package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.ExpenseEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseEntity> getAllExpenses(){
        return expenseRepository.findAll();
    }

    public Optional<ExpenseEntity> getExpenseById(Long id){
        return expenseRepository.findById(id);
    }

    public ExpenseEntity createExpense(ExpenseEntity expense){
        return expenseRepository.save(expense);
    }

    public void deleteExpenseById(Long id){
        expenseRepository.deleteById(id);
    }

    public ExpenseEntity updateExpense(Long id, ExpenseEntity expenseDetails){
        ExpenseEntity expense = expenseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Gasto não encontrado"));

        if (expense != null){
            expense.setName(expenseDetails.getName());
            expense.setCategory(expense.getCategory());
            expense.setPrice(expense.getPrice());
            expense.setUser(expenseDetails.getUser());
        }

        return expenseRepository.save(expense);
    }
}
