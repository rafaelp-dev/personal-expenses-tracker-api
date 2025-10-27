package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "O nome do usuário não pode estar vazio.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "O email do usuário não pode estar vazio.")
    @Column(nullable = false)
    private String email;

    public UserEntity(){}

    public UserEntity(String name, String email){
        this.name = name;
        this.email = email;
    }
}
