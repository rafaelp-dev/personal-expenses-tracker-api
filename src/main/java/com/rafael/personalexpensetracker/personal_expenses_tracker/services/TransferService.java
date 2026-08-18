package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.TransferRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.TransferResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.*;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final BalanceService balanceService;

    public TransferService(TransferRepository transferRepository,
                           AuthenticatedUserService authenticatedUserService,
                           BalanceService balanceService) {
        this.transferRepository = transferRepository;
        this.authenticatedUserService = authenticatedUserService;
        this.balanceService = balanceService;
    }

    @Transactional
    public TransferResponseDto create(TransferRequestDto request, String email) {
        UserEntity user = authenticatedUserService.require(email);
        SavingsBoxEntity box = balanceService.requireBox(user.getUserId(), request.savingsBoxId());

        if (request.direction() == TransferDirection.MAIN_TO_SAVINGS_BOX) {
            balanceService.debit(user, BalanceSource.MAIN, null, request.amount());
            balanceService.credit(user, BalanceSource.SAVINGS_BOX, box.getId(), request.amount());
        } else {
            balanceService.debit(user, BalanceSource.SAVINGS_BOX, box.getId(), request.amount());
            balanceService.credit(user, BalanceSource.MAIN, null, request.amount());
        }

        return toResponse(transferRepository.save(
                new TransferEntity(request.amount(), request.direction(), user, box)));
    }

    public List<TransferResponseDto> findByUser(String email) {
        return findByUser(authenticatedUserService.require(email).getUserId());
    }

    public List<TransferResponseDto> findByUser(Long userId) {
        return transferRepository.findByUser_UserIdOrderByDateDesc(userId).stream()
                .map(this::toResponse).toList();
    }

    public Page<TransferResponseDto> findByUser(String email, Pageable pageable) {
        Long userId = authenticatedUserService.require(email).getUserId();
        return transferRepository.findByUser_UserId(userId, pageable).map(this::toResponse);
    }

    private TransferResponseDto toResponse(TransferEntity transfer) {
        return new TransferResponseDto(transfer.getId(), transfer.getAmount(), transfer.getDirection(),
                transfer.getDate(), transfer.getSavingsBox().getId(), transfer.getSavingsBox().getName());
    }
}
