package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.UserRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.UserResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers(){
        List<UserEntity> userEntityList = userRepository.findAll();

        if (userEntityList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK);
        }

        return userEntityList;
    }

    public UserResponseDto getUserById(Long id){
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado."));

        return new UserResponseDto(userEntity.getUserId(), userEntity.getName(), userEntity.getEmail());
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto){
        UserEntity verifyEmail = userRepository.findByEmail(userRequestDto.email());

        if (verifyEmail != null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com email: " + userRequestDto.email() + " já existe");
        }

        UserEntity userEntity = new UserEntity(userRequestDto.name(),
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
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado."));

        userRepository.deleteById(id);
    }

    public UserEntity updateUser(Long id, UserEntity userDetails){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado."));

        if (userDetails.getName() != null) user.setName(userDetails.getName());
        if (userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());

        return userRepository.save(user);
    }
}
