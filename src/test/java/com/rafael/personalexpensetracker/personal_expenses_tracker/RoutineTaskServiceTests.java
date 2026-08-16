package com.rafael.personalexpensetracker.personal_expenses_tracker;

import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.RoutineTaskCompletionRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.RoutineTaskRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.dtos.request.UserRequestDto;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.RoutineTaskService;
import com.rafael.personalexpensetracker.personal_expenses_tracker.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:routine-task-test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "app.auth.jwt-secret=test-secret-with-at-least-32-characters"
})
@Transactional
class RoutineTaskServiceTests {
    @Autowired RoutineTaskService routineTaskService;
    @Autowired UserService userService;

    @Test
    void shouldCreateIndependentOccurrencesForEachScheduledDate() {
        String email = createUser("routine@example.com");
        LocalDate monday = LocalDate.of(2026, 8, 17);
        var task = routineTaskService.create(new RoutineTaskRequestDto(
                "Arrumar o guarda-roupa", Set.of(DayOfWeek.MONDAY), monday), email);

        assertEquals(1, routineTaskService.checklist(email, monday).size());
        assertFalse(routineTaskService.checklist(email, monday).getFirst().completed());

        routineTaskService.setCompleted(task.id(), new RoutineTaskCompletionRequestDto(monday, true), email);

        assertTrue(routineTaskService.checklist(email, monday).getFirst().completed());
        assertFalse(routineTaskService.checklist(email, monday.plusWeeks(1)).getFirst().completed());
        assertTrue(routineTaskService.checklist(email, monday.plusDays(1)).isEmpty());
    }

    @Test
    void shouldKeepMissedPastTaskPending() {
        String email = createUser("pending@example.com");
        LocalDate tuesday = LocalDate.of(2026, 8, 11);
        routineTaskService.create(new RoutineTaskRequestDto(
                "Lavar os tênis", Set.of(DayOfWeek.TUESDAY), tuesday), email);

        var pastChecklist = routineTaskService.checklist(email, tuesday);

        assertEquals(1, pastChecklist.size());
        assertFalse(pastChecklist.getFirst().completed());
    }

    @Test
    void shouldRejectCompletionOnUnscheduledDay() {
        String email = createUser("invalid-day@example.com");
        LocalDate monday = LocalDate.of(2026, 8, 17);
        var task = routineTaskService.create(new RoutineTaskRequestDto(
                "Academia", Set.of(DayOfWeek.MONDAY), monday), email);

        assertThrows(ResponseStatusException.class, () -> routineTaskService.setCompleted(
                task.id(), new RoutineTaskCompletionRequestDto(monday.plusDays(1), true), email));
    }

    @Test
    void shouldNotAllowAnotherUserToChangeTask() {
        String ownerEmail = createUser("owner@example.com");
        String otherEmail = createUser("other@example.com");
        LocalDate monday = LocalDate.of(2026, 8, 17);
        var task = routineTaskService.create(new RoutineTaskRequestDto(
                "Tarefa privada", Set.of(DayOfWeek.MONDAY), monday), ownerEmail);

        assertThrows(ResponseStatusException.class, () -> routineTaskService.setCompleted(
                task.id(), new RoutineTaskCompletionRequestDto(monday, true), otherEmail));
    }

    private String createUser(String email) {
        userService.createUser(new UserRequestDto("Test", email, "test-password"));
        return email;
    }
}
