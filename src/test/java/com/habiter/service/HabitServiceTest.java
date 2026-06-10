package com.habiter.service;

import com.habiter.dto.HabitRequestDTO;
import com.habiter.dto.HabitResponseDTO;
import com.habiter.exception.ResourceNotFoundException;
import com.habiter.model.Habit;
import com.habiter.model.User;
import com.habiter.repository.HabitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private HabitService habitService;

    private User user;
    private Habit habit;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Tomi");
        user.setLastName("B");
        user.setEmail("tomi@gmail.com");
        user.setPassword("hashedpassword");

        habit = new Habit();
        habit.setId(1L);
        habit.setName("Hacer ejercicio");
        habit.setDescription("30 minutos de cardio");
        habit.setFrequency(Habit.Frequency.DAILY);
        habit.setActive(true);
        habit.setCreatedAt(LocalDateTime.now());
        habit.setUser(user);
    }

    @Test
    void findAll_deberiaRetornarListaDeHabitsDelUsuario() {
        // Arrange
        when(habitRepository.findByUserId(1L)).thenReturn(List.of(habit));

        // Act
        List<HabitResponseDTO> result = habitService.findAll(user);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Hacer ejercicio", result.get(0).name());
        verify(habitRepository, times(1)).findByUserId(1L);
    }

    @Test
    void findById_deberiaRetornarHabitCuandoExiste() {
        // Arrange
        when(habitRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(habit));

        // Act
        HabitResponseDTO result = habitService.findById(1L, user);

        // Assert
        assertNotNull(result);
        assertEquals("Hacer ejercicio", result.name());
        assertEquals("DAILY", result.frequency());
    }

    @Test
    void findById_deberiaLanzarExcepcionCuandoNoExiste() {
        // Arrange
        when(habitRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                habitService.findById(99L, user)
        );
    }

    @Test
    void save_deberiaCrearHabitYAsociarloAlUsuario() {
        // Arrange
        HabitRequestDTO request = new HabitRequestDTO("Leer", "30 páginas", "DAILY");
        when(habitRepository.save(any(Habit.class))).thenReturn(habit);

        // Act
        HabitResponseDTO result = habitService.save(request, user);

        // Assert
        assertNotNull(result);
        verify(habitRepository, times(1)).save(any(Habit.class));
    }

    @Test
    void deleteById_deberiaEliminarHabitExistente() {
        // Arrange
        when(habitRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(habit));

        // Act
        habitService.deleteById(1L, user);

        // Assert
        verify(habitRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_deberiaLanzarExcepcionCuandoHabitNoPertenaceAlUsuario() {
        // Arrange
        when(habitRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                habitService.deleteById(99L, user)
        );
        verify(habitRepository, never()).deleteById(any());
    }
}