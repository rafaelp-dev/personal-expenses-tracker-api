package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response;

public record LoginResponseDto(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}
