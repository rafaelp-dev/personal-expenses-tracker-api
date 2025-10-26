package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.ExpenseRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.ExpenseResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.ExpenseEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.ExpenseRepository;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository){
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public List<ExpenseResponseDto> getAllExpenses(){
        List<ExpenseEntity> expenseEntityList = expenseRepository.findAll();

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

    public ExpenseResponseDto createExpense(ExpenseRequestDto expenseRequestDto){
        UserEntity userEntity = userRepository.findById(expenseRequestDto.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + expenseRequestDto.userId() + " não encontrado."));

        ExpenseEntity expenseEntity = new ExpenseEntity(
                expenseRequestDto.name(),
                expenseRequestDto.category(),
                expenseRequestDto.price(),
                userEntity
        );

        expenseRepository.save(expenseEntity);

        return new ExpenseResponseDto(
                expenseEntity.getExpenseId(),
                expenseEntity.getName(),
                expenseEntity.getCategory(),
                expenseEntity.getPrice(),
                expenseEntity.getDate(),
                userEntity.getName()
        );
    }

    public void deleteExpenseById(Long id){
        if (!expenseRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto com ID: " + id + " não encontrado.");
        }

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
