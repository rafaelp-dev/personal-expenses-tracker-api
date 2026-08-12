package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.BalanceSource;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.SavingsBoxEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.SavingsBoxRepository;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class BalanceService {
    private final UserRepository userRepository;
    private final SavingsBoxRepository savingsBoxRepository;

    public BalanceService(UserRepository userRepository, SavingsBoxRepository savingsBoxRepository) {
        this.userRepository = userRepository;
        this.savingsBoxRepository = savingsBoxRepository;
    }

    public SavingsBoxEntity credit(UserEntity user, BalanceSource destination, Long savingsBoxId, BigDecimal amount) {
        requirePositiveAmount(amount);
        if (destination == BalanceSource.MAIN) {
            user.setBalance(user.getBalance().add(amount));
            userRepository.save(user);
            return null;
        }

        SavingsBoxEntity box = requireBox(user.getUserId(), savingsBoxId);
        box.setBalance(box.getBalance().add(amount));
        return savingsBoxRepository.save(box);
    }

    public SavingsBoxEntity debit(UserEntity user, BalanceSource source, Long savingsBoxId, BigDecimal amount) {
        requirePositiveAmount(amount);
        if (source == BalanceSource.MAIN) {
            ensureFunds(user.getBalance(), amount, "Saldo principal insuficiente.");
            user.setBalance(user.getBalance().subtract(amount));
            userRepository.save(user);
            return null;
        }

        SavingsBoxEntity box = requireBox(user.getUserId(), savingsBoxId);
        ensureFunds(box.getBalance(), amount, "Saldo insuficiente na caixinha.");
        box.setBalance(box.getBalance().subtract(amount));
        return savingsBoxRepository.save(box);
    }

    public SavingsBoxEntity requireBox(Long userId, Long savingsBoxId) {
        if (savingsBoxId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O ID da caixinha é obrigatório.");
        }
        return savingsBoxRepository.findByIdAndUser_UserId(savingsBoxId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Caixinha não encontrada para este usuário."
                ));
    }

    private void ensureFunds(BigDecimal balance, BigDecimal amount, String message) {
        if (balance.compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, message);
        }
    }

    private void requirePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor deve ser maior que zero.");
        }
    }
}
