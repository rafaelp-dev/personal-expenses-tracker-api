package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

public record UserResponseDto(
        Long id,
        String name,
        String email
) {
}
