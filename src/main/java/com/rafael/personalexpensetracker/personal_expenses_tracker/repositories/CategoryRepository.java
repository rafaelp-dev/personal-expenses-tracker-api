package com.rafael.personalexpensetracker.personal_expenses_tracker.repositories;

import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByUser_UserId(Long userId);
    List<CategoryEntity> findByUser_UserIdAndType(Long userId, CategoryType type);
    boolean existsByUser_UserIdAndTypeAndNameIgnoreCase(Long userId, CategoryType type, String name);
}
