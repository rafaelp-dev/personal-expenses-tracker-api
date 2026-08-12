package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryType;

public record CategoryResponseDto(Long id, String name, CategoryType type, Long userId) {}
