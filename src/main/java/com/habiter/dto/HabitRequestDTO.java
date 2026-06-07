package com.habiter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record HabitRequestDTO(
        @NotBlank(message = "El nombre no puede estar vacío")
        String name,

        String description,

        @NotBlank(message = "La frecuencia no puede estar vacía")
        @Pattern(regexp = "DAILY|WEEKLY|MONTHLY", message = "La frecuencia debe ser DAILY, WEEKLY o MONTHLY")
        String frequency
) {}