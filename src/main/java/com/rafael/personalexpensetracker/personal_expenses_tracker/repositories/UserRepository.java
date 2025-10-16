package com.rafael.personalexpensetracker.personal_expenses_tracker.repositories;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
