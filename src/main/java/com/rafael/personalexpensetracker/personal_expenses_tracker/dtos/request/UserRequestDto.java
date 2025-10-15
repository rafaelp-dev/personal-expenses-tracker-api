package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank
        String name
) {
}
