package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Getter
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferDirection direction;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "savings_box_id", nullable = false)
    private SavingsBoxEntity savingsBox;

    protected TransferEntity() {}

    public TransferEntity(BigDecimal amount, TransferDirection direction, UserEntity user, SavingsBoxEntity savingsBox) {
        this.amount = amount;
        this.direction = direction;
        this.user = user;
        this.savingsBox = savingsBox;
        this.date = LocalDateTime.now();
    }
}
