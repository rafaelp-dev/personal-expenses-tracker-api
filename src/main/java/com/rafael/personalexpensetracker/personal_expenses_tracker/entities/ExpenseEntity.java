package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Getter
@Setter
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long expenseId;

    @NotBlank(message = "O nome da despesa não pode ser vazio ou nulo.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "A categoria da despesa não pode ser vazia ou nula.")
    @Column(nullable = false)
    private String category;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    @Column(nullable = false)
    private Integer price;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public ExpenseEntity(){
        this.date = LocalDateTime.now();
    }

    public ExpenseEntity(String name, String category, Integer price, UserEntity user) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = LocalDateTime.now();
        this.user = user;
    }
}
