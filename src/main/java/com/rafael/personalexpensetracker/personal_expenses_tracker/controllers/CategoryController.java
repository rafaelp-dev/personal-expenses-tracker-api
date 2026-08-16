package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.CategoryRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.CategoryResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryType;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@Valid @RequestBody CategoryRequestDto request, Authentication authentication) {
        return categoryService.create(request, authentication.getName());
    }

    @GetMapping
    public List<CategoryResponseDto> findByUser(@RequestParam(required = false) CategoryType type,
                                                Authentication authentication) {
        return categoryService.findByUser(authentication.getName(), type);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication authentication) {
        categoryService.delete(id, authentication.getName());
    }
}
