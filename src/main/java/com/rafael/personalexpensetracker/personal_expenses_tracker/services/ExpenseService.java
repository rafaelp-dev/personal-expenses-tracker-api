package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.ExpenseResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.ExpenseEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.ExpenseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseResponseDto> getAllExpenses(){
        List<ExpenseEntity> expenseEntityList = expenseRepository.findAll();

        if (expenseEntityList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK);
        }

        return expenseEntityList.stream()
                .map(expense-> new ExpenseResponseDto(
                        expense.getExpenseId(),
                        expense.getName(),
                        expense.getCategory(),
                        expense.getPrice(),
                        expense.getDate(),
                        expense.getUser().getName()))
                .toList();
    }

    public ExpenseResponseDto getExpenseById(Long id){
        ExpenseEntity expenseEntity = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto com ID: " + id + " não encontrado."));

        return new ExpenseResponseDto(
                expenseEntity.getExpenseId(),
                expenseEntity.getName(),
                expenseEntity.getCategory(),
                expenseEntity.getPrice(),
                expenseEntity.getDate(),
                expenseEntity.getUser().getName()
        );
    }

    public ExpenseEntity createExpense(ExpenseEntity expense){
        return expenseRepository.save(expense);
    }

    public void deleteExpenseById(Long id){
//        ExpenseEntity expenseEntity = expenseRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto com ID: " + id + " não encontrado."));

        expenseRepository.deleteById(id);
    }

    public ExpenseEntity updateExpense(Long id, ExpenseEntity expenseDetails){
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto com ID: " + id + " não encontrado."));

        if (expense.getName() != null) expense.setName(expenseDetails.getName());
        if (expense.getCategory() != null) expense.setCategory(expenseDetails.getCategory());
        if (expense.getPrice() != null) expense.setPrice(expenseDetails.getPrice());
        if (expense.getUser() != null) expense.setUser(expenseDetails.getUser());

        return expenseRepository.save(expense);
    }

    public List<ExpenseEntity> findByUserId(Long id){
        List<ExpenseEntity> expenseEntityList = expenseRepository.findByUser_UserId(id);

        if (expenseEntityList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK);
        }

        return expenseEntityList;
    }
}
