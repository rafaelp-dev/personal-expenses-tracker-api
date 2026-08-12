package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.LoginRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.RegisterRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.UserRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.LoginResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.RegisterResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.UserResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.JwtService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseDto register(@Valid @RequestBody RegisterRequestDto request) {
        UserResponseDto user = userService.createUser(
                new UserRequestDto(request.name(), request.email(), request.password())
        );
        return new RegisterResponseDto(user.id(), user.name(), user.email());
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
        }

        return new LoginResponseDto(
                jwtService.generateToken(request.email()),
                "Bearer",
                jwtService.getExpirationSeconds()
        );
    }
}
