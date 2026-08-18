package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.TransferRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.TransferResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponseDto create(@Valid @RequestBody TransferRequestDto request,
                                      Authentication authentication) {
        return transferService.create(request, authentication.getName());
    }

    @GetMapping
    public List<TransferResponseDto> list(Authentication authentication) {
        return transferService.findByUser(authentication.getName());
    }

    @GetMapping("/page")
    public Page<TransferResponseDto> page(Authentication authentication, Pageable pageable) {
        return transferService.findByUser(authentication.getName(), pageable);
    }
}
