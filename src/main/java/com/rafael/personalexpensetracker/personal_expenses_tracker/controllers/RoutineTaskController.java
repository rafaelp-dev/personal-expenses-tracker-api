package com.rafael.personalexpensetracker.personal_expenses_tracker.controllers;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.RoutineTaskCompletionRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.RoutineTaskRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.RoutineChecklistItemResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.RoutineTaskResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.RoutineTaskService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/routine-tasks")
public class RoutineTaskController {
    private final RoutineTaskService routineTaskService;

    public RoutineTaskController(RoutineTaskService routineTaskService) {
        this.routineTaskService = routineTaskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoutineTaskResponseDto create(@Valid @RequestBody RoutineTaskRequestDto request, Authentication authentication) {
        return routineTaskService.create(request, authentication.getName());
    }

    @GetMapping
    public List<RoutineTaskResponseDto> list(Authentication authentication) {
        return routineTaskService.listActive(authentication.getName());
    }

    @GetMapping("/checklist")
    public List<RoutineChecklistItemResponseDto> checklist(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {
        return routineTaskService.checklist(authentication.getName(), date == null ? LocalDate.now() : date);
    }

    @PatchMapping("/{taskId}/completion")
    public RoutineChecklistItemResponseDto setCompleted(@PathVariable Long taskId,
                                                         @Valid @RequestBody RoutineTaskCompletionRequestDto request,
                                                         Authentication authentication) {
        return routineTaskService.setCompleted(taskId, request, authentication.getName());
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long taskId, Authentication authentication) {
        routineTaskService.delete(taskId, authentication.getName());
    }
}
