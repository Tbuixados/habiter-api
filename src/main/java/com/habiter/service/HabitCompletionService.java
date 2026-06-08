package com.habiter.service;

import com.habiter.dto.HabitCompletionRequestDTO;
import com.habiter.dto.HabitCompletionResponseDTO;
import com.habiter.exception.ResourceNotFoundException;
import com.habiter.model.Habit;
import com.habiter.model.HabitCompletion;
import com.habiter.repository.HabitCompletionRepository;
import com.habiter.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitCompletionService {

    private final HabitCompletionRepository completionRepository;
    private final HabitRepository habitRepository;

    public HabitCompletionService(HabitCompletionRepository completionRepository,
                                  HabitRepository habitRepository) {
        this.completionRepository = completionRepository;
        this.habitRepository = habitRepository;
    }

    public HabitCompletionResponseDTO complete(Long habitId, HabitCompletionRequestDTO request) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Hábito no encontrado con id: " + habitId));

        HabitCompletion completion = new HabitCompletion();
        completion.setHabit(habit);
        completion.setNotes(request.notes());

        return toResponseDTO(completionRepository.save(completion));
    }

    public List<HabitCompletionResponseDTO> findByHabitId(Long habitId) {
        if (!habitRepository.existsById(habitId)) {
            throw new ResourceNotFoundException("Hábito no encontrado con id: " + habitId);
        }
        return completionRepository.findByHabitId(habitId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private HabitCompletionResponseDTO toResponseDTO(HabitCompletion completion) {
        return new HabitCompletionResponseDTO(
                completion.getId(),
                completion.getHabit().getId(),
                completion.getHabit().getName(),
                completion.getNotes(),
                completion.getCompletedAt()
        );
    }
}