package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.CategoryRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.CategoryResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.CategoryType;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public CategoryService(CategoryRepository categoryRepository, AuthenticatedUserService authenticatedUserService) {
        this.categoryRepository = categoryRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    public CategoryResponseDto create(CategoryRequestDto request, String email) {
        var user = authenticatedUserService.require(email);
        String name = request.name().trim();
        if (categoryRepository.existsByUser_UserIdAndTypeAndNameIgnoreCase(user.getUserId(), request.type(), name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma categoria com esse nome e tipo.");
        }
        return toResponse(categoryRepository.save(new CategoryEntity(name, request.type(), user)));
    }

    public List<CategoryResponseDto> findByUser(String email, CategoryType type) {
        Long userId = authenticatedUserService.require(email).getUserId();
        var categories = type == null
                ? categoryRepository.findByUser_UserId(userId)
                : categoryRepository.findByUser_UserIdAndType(userId, type);
        return categories.stream().map(this::toResponse).toList();
    }

    public void delete(Long id, String email) {
        Long userId = authenticatedUserService.require(email).getUserId();
        CategoryEntity category = findForUser(id, userId);
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

    private CategoryEntity findForUser(Long id, Long userId) {
        CategoryEntity category = find(id);
        if (!category.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada.");
        }
        return category;
    }

    private CategoryResponseDto toResponse(CategoryEntity category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getType(),
                category.getUser().getUserId());
    }
}
