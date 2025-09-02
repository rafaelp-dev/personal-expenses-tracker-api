package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
}
