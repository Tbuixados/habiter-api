package com.habiter.service;

import com.habiter.dto.AuthResponseDTO;
import com.habiter.dto.LoginRequestDTO;
import com.habiter.dto.RegisterRequestDTO;
import com.habiter.exception.BusinessException;
import com.habiter.model.User;
import com.habiter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Tomi");
        user.setLastName("B");
        user.setEmail("tomi@gmail.com");
        user.setPassword("hashedpassword");
    }

    @Test
    void register_deberiaRegistrarUsuarioYDevolverToken() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Tomi", "B", "tomi@gmail.com", "123456");
        when(userRepository.findByEmail("tomi@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        // Act
        AuthResponseDTO result = authService.register(request);

        // Assert
        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        assertEquals("tomi@gmail.com", result.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_deberiaLanzarExcepcionSiEmailYaExiste() {
        // Arrange
        RegisterRequestDTO request = new RegisterRequestDTO("Tomi", "B", "tomi@gmail.com", "123456");
        when(userRepository.findByEmail("tomi@gmail.com")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(BusinessException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_deberiaAutenticarYDevolverToken() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("tomi@gmail.com", "123456");
        when(userRepository.findByEmail("tomi@gmail.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        // Act
        AuthResponseDTO result = authService.login(request);

        // Assert
        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_deberiaLanzarExcepcionConCredencialesInvalidas() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("tomi@gmail.com", "wrongpassword");
        doThrow(new BadCredentialsException("Credenciales inválidas"))
                .when(authenticationManager).authenticate(any());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(request));
        verify(userRepository, never()).findByEmail(any());
    }
}