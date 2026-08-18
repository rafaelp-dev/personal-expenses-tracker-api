package com.rafael.personalexpensetracker.personal_expenses_tracker;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.SavingsBoxRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.TransferRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.UserRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.TransferDirection;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.SavingsBoxService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.TransferService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:transfer-test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "app.auth.jwt-secret=test-secret-with-at-least-32-characters"
})
@Transactional
class TransferServiceTests {
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired SavingsBoxService savingsBoxService;
    @Autowired TransferService transferService;

    @Test
    void shouldTransferInBothDirectionsWithoutChangingTotalBalance() {
        String email = "transfer@example.com";
        userService.createUser(new UserRequestDto("Test", email, "test-password"));
        var user = userRepository.findByEmail(email).orElseThrow();
        user.setBalance(new BigDecimal("1000.00"));
        userRepository.save(user);
        var box = savingsBoxService.create(new SavingsBoxRequestDto("Reserva", new BigDecimal("100.00")), email);

        transferService.create(new TransferRequestDto(
                new BigDecimal("250.00"), TransferDirection.MAIN_TO_SAVINGS_BOX, box.id()), email);
        var afterDeposit = savingsBoxService.getBalance(email);
        assertEquals(new BigDecimal("750.00"), afterDeposit.mainBalance());
        assertEquals(new BigDecimal("350.00"), afterDeposit.savingsBoxesBalance());
        assertEquals(new BigDecimal("1100.00"), afterDeposit.totalBalance());

        transferService.create(new TransferRequestDto(
                new BigDecimal("50.00"), TransferDirection.SAVINGS_BOX_TO_MAIN, box.id()), email);
        var afterWithdrawal = savingsBoxService.getBalance(email);
        assertEquals(new BigDecimal("800.00"), afterWithdrawal.mainBalance());
        assertEquals(new BigDecimal("300.00"), afterWithdrawal.savingsBoxesBalance());
        assertEquals(2, transferService.findByUser(email).size());
    }

    @Test
    void shouldRejectTransferWithoutEnoughFunds() {
        String email = "insufficient@example.com";
        userService.createUser(new UserRequestDto("Test", email, "test-password"));
        var box = savingsBoxService.create(new SavingsBoxRequestDto("Reserva", BigDecimal.ZERO), email);

        assertThrows(ResponseStatusException.class, () -> transferService.create(new TransferRequestDto(
                BigDecimal.ONE, TransferDirection.SAVINGS_BOX_TO_MAIN, box.id()), email));
    }
}
