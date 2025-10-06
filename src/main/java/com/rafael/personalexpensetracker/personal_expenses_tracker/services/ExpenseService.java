package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.ExpenseEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseEntity> getAllExpenses(){
        List<ExpenseEntity> expenseEntityList = expenseRepository.findAll();

        if (expenseEntityList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Lista de despesas vazia.");
        }

        return expenseEntityList;
    }

    public ExpenseEntity getExpenseById(Long id){
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado."));
    }

    public ExpenseEntity createExpense(ExpenseEntity expense){
        return expenseRepository.save(expense);
    }

    public void deleteExpenseById(Long id){
        ExpenseEntity expenseEntity = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado."));

        expenseRepository.deleteById(id);
    }

    public ExpenseEntity updateExpense(Long id, ExpenseEntity expenseDetails){
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado."));

        if (expense != null){
            expense.setName(expenseDetails.getName());
            expense.setCategory(expenseDetails.getCategory());
            expense.setPrice(expenseDetails.getPrice());
            expense.setUser(expenseDetails.getUser());
        }

        return expenseRepository.save(expense);
    }

    public List<ExpenseEntity> findByUserId(Long id){
        List<ExpenseEntity> expenseEntityList = expenseRepository.findAll();

        if (expenseEntityList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Lista de gastos de usuário com ID: " + id + " vazia.");
        }

        return expenseRepository.findByUser_UserId(id);
    }
}
