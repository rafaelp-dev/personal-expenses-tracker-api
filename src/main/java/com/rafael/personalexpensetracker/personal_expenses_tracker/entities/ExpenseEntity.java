package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Getter
@Setter
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @NotBlank(message = "O nome do gasto não pode estar vazio.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "A categoria não pode estar vazia.")
    @Column(nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    @NotNull(message = "O preço do gasto não pode estar vazio.")
    @Positive(message = "O preço do gasto deve ser maior do que 0.")
    @Column(nullable = false)
    private BigDecimal price;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BalanceSource source = BalanceSource.MAIN;

    @ManyToOne
    @JoinColumn(name = "savings_box_id")
    private SavingsBoxEntity savingsBox;

    public ExpenseEntity(){
        this.date = LocalDateTime.now();
    }

    public ExpenseEntity(String name, CategoryEntity category, BigDecimal price, UserEntity user,
                         BalanceSource source, SavingsBoxEntity savingsBox) {
        this.name = name;
        this.category = category.getName();
        this.categoryEntity = category;
        this.price = price;
        this.date = LocalDateTime.now();
        this.user = user;
        this.source = source;
        this.savingsBox = savingsBox;
    }
}
