package com.habiter.controller;

import com.habiter.dto.HabitRequestDTO;
import com.habiter.dto.HabitResponseDTO;
import com.habiter.service.HabitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<HabitResponseDTO>> findAll() {
        return ResponseEntity.ok(habitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(habitService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HabitResponseDTO> save(@RequestBody HabitRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitService.save(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        habitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}