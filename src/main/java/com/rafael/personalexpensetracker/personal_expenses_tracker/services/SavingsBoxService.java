package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.SavingsBoxRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.BalanceResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.SavingsBoxResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.SavingsBoxEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.SavingsBoxRepository;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SavingsBoxService {
    private final SavingsBoxRepository savingsBoxRepository;
    private final UserRepository userRepository;

    public SavingsBoxService(SavingsBoxRepository savingsBoxRepository, UserRepository userRepository) {
        this.savingsBoxRepository = savingsBoxRepository;
        this.userRepository = userRepository;
    }

    public SavingsBoxResponseDto create(SavingsBoxRequestDto request) {
        UserEntity user = findUser(request.userId());
        return toResponse(savingsBoxRepository.save(
                new SavingsBoxEntity(request.name(), request.initialBalance(), user)
        ));
    }

    public List<SavingsBoxResponseDto> findByUserId(Long userId) {
        findUser(userId);
        return savingsBoxRepository.findByUser_UserId(userId).stream().map(this::toResponse).toList();
    }

    public BalanceResponseDto getBalance(Long userId) {
        UserEntity user = findUser(userId);
        List<SavingsBoxResponseDto> boxes = findByUserId(userId);
        BigDecimal boxesBalance = boxes.stream()
                .map(SavingsBoxResponseDto::balance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new BalanceResponseDto(
                userId, user.getBalance(), boxesBalance, user.getBalance().add(boxesBalance), boxes
        );
    }

    private UserEntity findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuário com ID " + id + " não encontrado."
        ));
    }

    private SavingsBoxResponseDto toResponse(SavingsBoxEntity box) {
        return new SavingsBoxResponseDto(box.getId(), box.getName(), box.getBalance(), box.getUser().getUserId());
    }
}
