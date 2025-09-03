package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<List<UserEntity>> getAllUsers(){
        List<UserEntity> userEntityList = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(userEntityList);
    }

    public ResponseEntity<UserEntity> getUserById(Long id){
        return userRepository.findById(id)
                .map(user -> ResponseEntity.status(HttpStatus.OK).body(user))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<UserEntity> createUser(UserEntity userEntity){
        return ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(userEntity));
    }
}
