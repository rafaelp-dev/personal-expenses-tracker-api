package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.CategoryRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.CategoryResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryType;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.CategoryRepository;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public CategoryResponseDto create(CategoryRequestDto request) {
        String name = request.name().trim();
        if (categoryRepository.existsByUser_UserIdAndTypeAndNameIgnoreCase(request.userId(), request.type(), name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma categoria com esse nome e tipo.");
        }
        var user = userRepository.findById(request.userId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        return toResponse(categoryRepository.save(new CategoryEntity(name, request.type(), user)));
    }

    public List<CategoryResponseDto> findByUser(Long userId, CategoryType type) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }
        var categories = type == null
                ? categoryRepository.findByUser_UserId(userId)
                : categoryRepository.findByUser_UserIdAndType(userId, type);
        return categories.stream().map(this::toResponse).toList();
    }

    public void delete(Long id) {
        CategoryEntity category = find(id);
        try {
            categoryRepository.delete(category);
            categoryRepository.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A categoria está sendo usada e não pode ser excluída.");
        }
    }

    public CategoryEntity findForUserAndType(Long id, Long userId, CategoryType type) {
        CategoryEntity category = find(id);
        if (!category.getUser().getUserId().equals(userId) || category.getType() != type) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria não pertence ao usuário ou possui tipo incompatível.");
        }
        return category;
    }

    private CategoryEntity find(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada."));
    }

    private CategoryResponseDto toResponse(CategoryEntity category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getType(),
                category.getUser().getUserId());
    }
}
