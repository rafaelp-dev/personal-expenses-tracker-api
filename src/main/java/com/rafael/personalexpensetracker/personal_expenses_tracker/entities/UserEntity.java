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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @NotBlank(message = "Nome do usuário não pode ser vazio ou nulo.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email não pode ser vazio ou nulo.")
    @Column(nullable = false)
    private String email;

    public UserEntity(){}

    public UserEntity(String name, String email){
        this.name = name;
        this.email = email;
    }
}
