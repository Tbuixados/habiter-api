package com.habiter.dto;

import java.time.LocalDateTime;

public record HabitResponseDTO(
        Long id,
        String name,
        String description,
        String frequency,
        boolean active,
        LocalDateTime createdAt
) {}