package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.UserRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.UserResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponseDto getUserById(Long id) {
        return toResponse(findUser(id));
    }

    public UserResponseDto createUser(UserRequestDto request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail.");
        }

        UserEntity user = new UserEntity(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password())
        );
        return toResponse(userRepository.save(user));
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID " + id + " não encontrado.");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto request) {
        UserEntity user = findUser(id);

        if (request.name() != null) user.setName(request.name());
        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail.");
            }
            user.setEmail(request.email());
        }
        if (request.password() != null) user.setPassword(passwordEncoder.encode(request.password()));

        return toResponse(userRepository.save(user));
    }

    private UserEntity findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário com ID " + id + " não encontrado."
                ));
    }

    private UserResponseDto toResponse(UserEntity user) {
        return new UserResponseDto(user.getUserId(), user.getName(), user.getEmail());
    }
}
