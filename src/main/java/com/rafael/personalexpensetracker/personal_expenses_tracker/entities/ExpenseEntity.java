package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
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

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    public ExpenseEntity(String name, String category, Integer price, LocalDateTime date, UserEntity userEntity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = date;
        this.userEntity = userEntity;
    }
}
