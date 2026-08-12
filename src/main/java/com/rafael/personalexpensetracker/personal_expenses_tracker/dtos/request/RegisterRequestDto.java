package com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "O nome não pode estar vazio.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String name,

        @NotBlank(message = "O e-mail não pode estar vazio.")
        @Email(message = "O e-mail deve ser válido.")
        String email,

        @NotBlank(message = "A senha não pode estar vazia.")
        @Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres.")
        String password
) {
}
