package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

public record RegisterResponseDto(
        Long id,
        String name,
        String email
) {
}
