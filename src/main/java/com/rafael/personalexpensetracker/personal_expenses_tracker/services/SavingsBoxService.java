package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.SavingsBoxRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.BalanceResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.SavingsBoxResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.SavingsBoxEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.SavingsBoxRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SavingsBoxService {
    private final SavingsBoxRepository savingsBoxRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public SavingsBoxService(SavingsBoxRepository savingsBoxRepository, AuthenticatedUserService authenticatedUserService) {
        this.savingsBoxRepository = savingsBoxRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    public SavingsBoxResponseDto create(SavingsBoxRequestDto request, String email) {
        UserEntity user = authenticatedUserService.require(email);
        return toResponse(savingsBoxRepository.save(
                new SavingsBoxEntity(request.name(), request.initialBalance(), user)
        ));
    }

    public List<SavingsBoxResponseDto> findByUser(String email) {
        Long userId = authenticatedUserService.require(email).getUserId();
        return savingsBoxRepository.findByUser_UserId(userId).stream().map(this::toResponse).toList();
    }

    public BalanceResponseDto getBalance(String email) {
        UserEntity user = authenticatedUserService.require(email);
        Long userId = user.getUserId();
        List<SavingsBoxResponseDto> boxes = savingsBoxRepository.findByUser_UserId(userId).stream().map(this::toResponse).toList();
        BigDecimal boxesBalance = boxes.stream()
                .map(SavingsBoxResponseDto::balance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new BalanceResponseDto(
                userId, user.getBalance(), boxesBalance, user.getBalance().add(boxesBalance), boxes
        );
    }

    private SavingsBoxResponseDto toResponse(SavingsBoxEntity box) {
        return new SavingsBoxResponseDto(box.getId(), box.getName(), box.getBalance(), box.getUser().getUserId());
    }
}
