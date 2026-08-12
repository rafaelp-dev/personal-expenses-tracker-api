package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.SavingsBoxRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.SavingsBoxResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.SavingsBoxService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/savings-boxes")
public class SavingsBoxController {
    private final SavingsBoxService savingsBoxService;

    public SavingsBoxController(SavingsBoxService savingsBoxService) {
        this.savingsBoxService = savingsBoxService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsBoxResponseDto create(@Valid @RequestBody SavingsBoxRequestDto request) {
        return savingsBoxService.create(request);
    }
}
