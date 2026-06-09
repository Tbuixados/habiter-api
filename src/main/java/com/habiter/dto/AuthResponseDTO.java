package com.habiter.dto;

public record AuthResponseDTO(
        String token,
        String firstName,
        String lastName,
        String email
) {}