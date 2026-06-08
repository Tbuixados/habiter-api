package com.habiter.controller;

import com.habiter.dto.HabitCompletionRequestDTO;
import com.habiter.dto.HabitCompletionResponseDTO;
import com.habiter.service.HabitCompletionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits/{habitId}/completions")
public class HabitCompletionController {

    private final HabitCompletionService completionService;

    public HabitCompletionController(HabitCompletionService completionService) {
        this.completionService = completionService;
    }

    @PostMapping
    public ResponseEntity<HabitCompletionResponseDTO> complete(
            @PathVariable Long habitId,
            @RequestBody HabitCompletionRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(completionService.complete(habitId, request));
    }

    @GetMapping
    public ResponseEntity<List<HabitCompletionResponseDTO>> findByHabitId(@PathVariable Long habitId) {
        return ResponseEntity.ok(completionService.findByHabitId(habitId));
    }
}