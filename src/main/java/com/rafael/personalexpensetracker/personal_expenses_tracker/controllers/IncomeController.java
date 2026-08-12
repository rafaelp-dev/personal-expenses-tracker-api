package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.IncomeRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.IncomeResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.IncomeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incomes")
public class IncomeController {
    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncomeResponseDto create(@Valid @RequestBody IncomeRequestDto request) {
        return incomeService.create(request);
    }
}
