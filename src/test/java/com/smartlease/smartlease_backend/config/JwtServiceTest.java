package com.smartlease.smartlease_backend.config;

import com.smartlease.smartlease_backend.model.Role;
import com.smartlease.smartlease_backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

  private JwtService jwtService;
  private User testUser;

  @BeforeEach
  void setUp() {
    jwtService = new JwtService();
    testUser = new User();
    testUser.setId(1L);
    testUser.setName("Test User");
    testUser.setEmail("test@example.com");
    testUser.setPassword("password123");
    testUser.setRole(Role.ROLE_TENANT);
  }

  @Test
  @DisplayName("generateToken should return a non-null, non-empty token")
  void generateToken_shouldReturnNonNullToken() {
    String token = jwtService.generateToken(testUser);

    assertNotNull(token);
    assertFalse(token.isEmpty());
  }

  @Test
  @DisplayName("extractUsername should return the email set in the token")
  void extractUsername_shouldReturnCorrectEmail() {
    String token = jwtService.generateToken(testUser);

    String extractedUsername = jwtService.extractUsername(token);

    assertEquals("test@example.com", extractedUsername);
  }

  @Test
  @DisplayName("isTokenValid should return true for a valid token and matching user")
  void isTokenValid_shouldReturnTrueForValidToken() {
    String token = jwtService.generateToken(testUser);

    boolean isValid = jwtService.isTokenValid(token, testUser);

    assertTrue(isValid);
  }

  @Test
  @DisplayName("isTokenValid should return false when username does not match")
  void isTokenValid_shouldReturnFalseWhenUsernameMismatch() {
    String token = jwtService.generateToken(testUser);

    // Create a different user with a different email
    User differentUser = new User();
    differentUser.setId(2L);
    differentUser.setEmail("other@example.com");
    differentUser.setPassword("password123");
    differentUser.setRole(Role.ROLE_TENANT);

    boolean isValid = jwtService.isTokenValid(token, differentUser);

    assertFalse(isValid);
  }

  @Test
  @DisplayName("Token should contain custom claims like role, id, and name")
  void generateToken_shouldContainCustomClaims() {
    String token = jwtService.generateToken(testUser);

    // If the token is valid and the username can be extracted, the claims were set
    String username = jwtService.extractUsername(token);
    assertEquals("test@example.com", username);

    // Verify we can extract the role claim
    String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
    assertEquals("ROLE_TENANT", role);

    // Verify we can extract the id claim
    Integer id = jwtService.extractClaim(token, claims -> claims.get("id", Integer.class));
    assertEquals(1, id);

    // Verify we can extract the name claim
    String name = jwtService.extractClaim(token, claims -> claims.get("name", String.class));
    assertEquals("Test User", name);
  }
}
