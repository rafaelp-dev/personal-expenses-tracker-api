package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
