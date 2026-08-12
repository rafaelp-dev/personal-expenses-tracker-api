package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.IncomeRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.IncomeResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.IncomeEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.SavingsBoxEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.IncomeRepository;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final BalanceService balanceService;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository, BalanceService balanceService) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
        this.balanceService = balanceService;
    }

    @Transactional
    public IncomeResponseDto create(IncomeRequestDto request) {
        UserEntity user = userRepository.findById(request.userId()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuário com ID " + request.userId() + " não encontrado."
        ));
        SavingsBoxEntity box = balanceService.credit(
                user, request.destination(), request.savingsBoxId(), request.amount()
        );
        return toResponse(incomeRepository.save(new IncomeEntity(
                request.description(), request.amount(), request.destination(), user, box
        )));
    }

    public List<IncomeResponseDto> findByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID " + userId + " não encontrado.");
        }
        return incomeRepository.findByUser_UserId(userId).stream().map(this::toResponse).toList();
    }

    private IncomeResponseDto toResponse(IncomeEntity income) {
        SavingsBoxEntity box = income.getSavingsBox();
        return new IncomeResponseDto(
                income.getId(), income.getDescription(), income.getAmount(), income.getDate(),
                income.getDestination(), income.getUser().getUserId(),
                box == null ? null : box.getId(), box == null ? null : box.getName()
        );
    }
}
