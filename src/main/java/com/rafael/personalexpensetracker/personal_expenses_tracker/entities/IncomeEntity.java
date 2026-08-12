package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "incomes")
@Getter
public class IncomeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BalanceSource destination;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "savings_box_id")
    private SavingsBoxEntity savingsBox;

    protected IncomeEntity() {
    }

    public IncomeEntity(String description, BigDecimal amount, CategoryEntity category, BalanceSource destination,
                        UserEntity user, SavingsBoxEntity savingsBox) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.destination = destination;
        this.user = user;
        this.savingsBox = savingsBox;
        this.date = LocalDateTime.now();
    }
}
