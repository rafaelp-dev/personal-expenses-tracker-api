package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.UserRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.UserResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers(){
        List<UserEntity> userEntityList = userRepository.findAll();

        return userEntityList
                .stream()
                .map(user -> new UserResponseDto(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail())
                )
                .toList();
    }

    public UserResponseDto getUserById(Long id){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado."));

        return new UserResponseDto(
                userEntity.getUserId(),
                userEntity.getName(),
                userEntity.getEmail()
        );
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto){
        UserEntity verifyEmail = userRepository.findByEmail(userRequestDto.email());

        if (verifyEmail != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário com email: " + userRequestDto.email() + " já existe.");
        }

        UserEntity userEntity = new UserEntity(
                userRequestDto.name(),
                userRequestDto.email()
        );

        userRepository.save(userEntity);

        return new UserResponseDto(
                userEntity.getUserId(),
                userEntity.getName(),
                userEntity.getEmail()
        );
    }

    public void deleteUserById(Long id){
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado.");
        }

        userRepository.existsById(id);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado."));

        if (userRequestDto.name() != null) user.setName(userRequestDto.name());
        if (userRequestDto.email() != null) user.setEmail(userRequestDto.email());

        userRepository.save(user);

        return new UserResponseDto(
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }
}
