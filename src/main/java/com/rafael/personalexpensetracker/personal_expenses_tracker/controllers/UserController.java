package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        List<UserEntity> userEntityList = userService.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(userEntityList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id){
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
