package com.rafael.personalexpensetracker.personal_expenses_tracker.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "O nome do usuário não pode estar vazio.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "O email do usuário não pode estar vazio.")
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    public UserEntity(){}

    public UserEntity(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = BigDecimal.ZERO;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
