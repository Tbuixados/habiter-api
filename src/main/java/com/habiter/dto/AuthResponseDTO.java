package com.habiter.dto;

public record AuthResponseDTO(
        String token,
        String name,
        String email
) {}