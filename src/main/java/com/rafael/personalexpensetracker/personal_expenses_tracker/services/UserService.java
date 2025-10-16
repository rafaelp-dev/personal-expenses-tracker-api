package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

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

    public List<UserEntity> getAllUsers(){
        List<UserEntity> userEntityList = userRepository.findAll();

        if (userEntityList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK);
        }

        return userEntityList;
    }

    public UserEntity getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com ID: " + id + " não encontrado."));
    }

    public UserEntity createUser(UserEntity user){
        UserEntity userEntity = userRepository.findByEmail(user.getEmail());

        if (userEntity != null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário com email: " + user.getEmail() + " já existe");
        }

        return userRepository.save(user);
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
