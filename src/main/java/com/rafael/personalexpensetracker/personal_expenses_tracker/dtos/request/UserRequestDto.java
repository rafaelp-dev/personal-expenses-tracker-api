package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank(message = "O nome do usuário não pode estar vazio.")
        String name,

        @NotBlank(message = "O email do usuário não pode estar vazio.")
        String email
) {
}
