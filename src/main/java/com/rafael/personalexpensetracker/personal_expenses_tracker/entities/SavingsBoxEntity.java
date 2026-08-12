package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "savings_boxes")
@Getter
@Setter
public class SavingsBoxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public SavingsBoxEntity() {
    }

    public SavingsBoxEntity(String name, BigDecimal balance, UserEntity user) {
        this.name = name;
        this.balance = balance;
        this.user = user;
    }
}
