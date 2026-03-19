package com.smartlease.smartlease_backend.service;

import com.smartlease.smartlease_backend.config.JwtService;
import com.smartlease.smartlease_backend.dto.AuthenticationRequest;
import com.smartlease.smartlease_backend.dto.AuthenticationResponse;
import com.smartlease.smartlease_backend.dto.RegisterRequest;
import com.smartlease.smartlease_backend.model.Role;
import com.smartlease.smartlease_backend.model.User;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import com.smartlease.smartlease_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private PropertyRepository propertyRepository;

  @InjectMocks
  private AuthenticationService authenticationService;

  private RegisterRequest registerRequest;
  private AuthenticationRequest loginRequest;
  private User savedUser;

  @BeforeEach
  void setUp() {
    registerRequest = new RegisterRequest("John Doe", "john@example.com", "password123", Role.ROLE_TENANT);

    loginRequest = new AuthenticationRequest("john@example.com", "password123");

    savedUser = new User();
    savedUser.setId(1L);
    savedUser.setName("John Doe");
    savedUser.setEmail("john@example.com");
    savedUser.setPassword("encodedPassword");
    savedUser.setRole(Role.ROLE_TENANT);
  }

  // ===================== REGISTER TESTS =====================

  @Test
    @DisplayName("register should save user and return JWT token")
    void register_shouldSaveUserAndReturnToken() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("mock-jwt-token");

        // Act
        AuthenticationResponse response = authenticationService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

  @Test
    @DisplayName("register should throw exception when email already exists")
    void register_shouldThrowExceptionWhenEmailAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(savedUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authenticationService.register(registerRequest));

        assertEquals("User with this email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

  @Test
    @DisplayName("register should encode password before saving")
    void register_shouldEncodePasswordBeforeSaving() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("mock-jwt-token");

        // Act
        authenticationService.register(registerRequest);

        // Assert
        verify(passwordEncoder, times(1)).encode("password123");
    }

  // ===================== LOGIN TESTS =====================

  @Test
    @DisplayName("login should authenticate and return JWT token")
    void login_shouldAuthenticateAndReturnToken() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(savedUser));
        when(jwtService.generateToken(savedUser)).thenReturn("mock-jwt-token");

        // Act
        AuthenticationResponse response = authenticationService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
    }

  @Test
    @DisplayName("login should throw exception for bad credentials")
    void login_shouldThrowExceptionForBadCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class,
                () -> authenticationService.login(loginRequest));
    }
}
