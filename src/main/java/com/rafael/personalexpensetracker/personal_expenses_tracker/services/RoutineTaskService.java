package com.rafael.personalexpensetracker.personal_expenses_tracker.services;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.RoutineTaskCompletionRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.RoutineTaskRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.RoutineChecklistItemResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.response.RoutineTaskResponseDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.RoutineTaskEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.RoutineTaskOccurrenceEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.entities.UserEntity;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.RoutineTaskOccurrenceRepository;
import com.rafael.personalexpensetracker.personal_expenses_tracker.repositories.RoutineTaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class RoutineTaskService {
    private final RoutineTaskRepository taskRepository;
    private final RoutineTaskOccurrenceRepository occurrenceRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public RoutineTaskService(RoutineTaskRepository taskRepository,
                              RoutineTaskOccurrenceRepository occurrenceRepository,
                              AuthenticatedUserService authenticatedUserService) {
        this.taskRepository = taskRepository;
        this.occurrenceRepository = occurrenceRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    public RoutineTaskResponseDto create(RoutineTaskRequestDto request, String email) {
        UserEntity user = authenticatedUserService.require(email);
        LocalDate startsOn = request.startsOn() == null ? LocalDate.now() : request.startsOn();
        return toTaskResponse(taskRepository.save(
                new RoutineTaskEntity(request.title().trim(), request.weekdays(), startsOn, user)));
    }

    public List<RoutineTaskResponseDto> listActive(String email) {
        Long userId = authenticatedUserService.require(email).getUserId();
        return taskRepository.findByUser_UserIdAndActiveTrueOrderByTitleAsc(userId)
                .stream().map(this::toTaskResponse).toList();
    }

    @Transactional
    public List<RoutineChecklistItemResponseDto> checklist(String email, LocalDate date) {
        Long userId = authenticatedUserService.require(email).getUserId();
        return taskRepository.findByUser_UserIdOrderByTitleAsc(userId).stream()
                .filter(task -> occursOn(task, date))
                .map(task -> occurrenceRepository.findByRoutineTask_IdAndDate(task.getId(), date)
                        .orElseGet(() -> occurrenceRepository.save(new RoutineTaskOccurrenceEntity(task, date))))
                .map(this::toChecklistResponse)
                .toList();
    }

    @Transactional
    public RoutineChecklistItemResponseDto setCompleted(Long taskId, RoutineTaskCompletionRequestDto request, String email) {
        Long userId = authenticatedUserService.require(email).getUserId();
        RoutineTaskEntity task = findTask(taskId, userId);
        if (!occursOn(task, request.date())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A tarefa não está programada para essa data.");
        }

        RoutineTaskOccurrenceEntity occurrence = occurrenceRepository
                .findByRoutineTask_IdAndDate(taskId, request.date())
                .orElseGet(() -> new RoutineTaskOccurrenceEntity(task, request.date()));
        occurrence.setCompleted(request.completed());
        occurrence.setCompletedAt(request.completed() ? OffsetDateTime.now() : null);
        return toChecklistResponse(occurrenceRepository.save(occurrence));
    }

    @Transactional
    public void delete(Long taskId, String email) {
        Long userId = authenticatedUserService.require(email).getUserId();
        RoutineTaskEntity task = findTask(taskId, userId);
        task.setActive(false);
        task.setEndsOn(LocalDate.now().minusDays(1));
        taskRepository.save(task);
    }

    private boolean occursOn(RoutineTaskEntity task, LocalDate date) {
        return !date.isBefore(task.getStartsOn())
                && (task.getEndsOn() == null || !date.isAfter(task.getEndsOn()))
                && task.getWeekdays().contains(date.getDayOfWeek());
    }

    private RoutineTaskEntity findTask(Long taskId, Long userId) {
        return taskRepository.findByIdAndUser_UserId(taskId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada."));
    }

    private RoutineTaskResponseDto toTaskResponse(RoutineTaskEntity task) {
        return new RoutineTaskResponseDto(task.getId(), task.getTitle(), task.getWeekdays(), task.getStartsOn(),
                task.isActive(), task.getUser().getUserId());
    }

    private RoutineChecklistItemResponseDto toChecklistResponse(RoutineTaskOccurrenceEntity occurrence) {
        return new RoutineChecklistItemResponseDto(occurrence.getRoutineTask().getId(),
                occurrence.getRoutineTask().getTitle(), occurrence.getDate(), occurrence.isCompleted(),
                occurrence.getCompletedAt());
    }
}
