package com.habiter.service;

import com.habiter.dto.HabitRequestDTO;
import com.habiter.dto.HabitResponseDTO;
import com.habiter.exception.ResourceNotFoundException;
import com.habiter.model.Habit;
import com.habiter.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    public List<HabitResponseDTO> findAll() {
        return habitRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public HabitResponseDTO findById(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hábito no encontrado con id: " + id));
        return toResponseDTO(habit);
    }

    public HabitResponseDTO save(HabitRequestDTO request) {
        Habit habit = new Habit();
        habit.setName(request.name());
        habit.setDescription(request.description());
        habit.setFrequency(Habit.Frequency.valueOf(request.frequency()));
        return toResponseDTO(habitRepository.save(habit));
    }

    public void deleteById(Long id) {
        habitRepository.deleteById(id);
    }

    private HabitResponseDTO toResponseDTO(Habit habit) {
        return new HabitResponseDTO(
                habit.getId(),
                habit.getName(),
                habit.getDescription(),
                habit.getFrequency().name(),
                habit.isActive(),
                habit.getCreatedAt()
        );
    }
}