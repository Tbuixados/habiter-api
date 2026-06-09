package com.habiter.controller;

import com.habiter.dto.HabitRequestDTO;
import com.habiter.dto.HabitResponseDTO;
import com.habiter.model.User;
import com.habiter.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    public ResponseEntity<List<HabitResponseDTO>> findAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(habitService.findAll(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitResponseDTO> findById(@PathVariable Long id,
                                                     @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(habitService.findById(id, user));
    }

    @PostMapping
    public ResponseEntity<HabitResponseDTO> save(@Valid @RequestBody HabitRequestDTO request,
                                                 @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.save(request, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitResponseDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody HabitRequestDTO request,
                                                   @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(habitService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id,
                                           @AuthenticationPrincipal User user) {
        habitService.deleteById(id, user);
        return ResponseEntity.noContent().build();
    }
}