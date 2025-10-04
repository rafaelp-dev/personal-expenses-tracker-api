package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        List<UserEntity> userEntityList = userService.getAllUsers();

        return ResponseEntity.ok().body(userEntityList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id){
        UserEntity userEntity = userService.getUserById(id);

        return ResponseEntity.ok().body(userEntity);
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@Valid @RequestBody UserEntity user){
        UserEntity userEntity = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserEntity user) {
        UserEntity userEntity = userService.updateUser(id, user);

        return ResponseEntity.ok().body(userEntity);
    }
}
