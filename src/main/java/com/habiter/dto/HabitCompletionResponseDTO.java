package com.habiter.dto;

import java.time.LocalDateTime;

public record HabitCompletionResponseDTO(
        Long id,
        Long habitId,
        String habitName,
        String notes,
        LocalDateTime completedAt
) {}