package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long expenseId;
    private Long userId;
    private String name;
    private String category;
    private Integer price;
    private LocalDateTime date;
}
