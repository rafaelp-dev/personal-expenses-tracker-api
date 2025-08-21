package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String name;

    @OneToMany
    private List<ExpenseEntity> expenses;
}
