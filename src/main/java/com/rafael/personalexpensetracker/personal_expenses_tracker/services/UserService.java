package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity getAllUsers(){
        List<UserEntity> userEntityList = userRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(userEntityList);
    }
}
