package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
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
    private String name;
    private String category;
    private Integer price;
    private LocalDateTime date;
    private Long userId;

    public ExpenseEntity(){}

    public ExpenseEntity(String name, String category, Integer price, Long userId) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = LocalDateTime.now();
        this.userId = userId;
    }
}
