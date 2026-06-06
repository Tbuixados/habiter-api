package com.habiter.dto;

public record HabitRequestDTO(
        String name,
        String description,
        String frequency
) {}