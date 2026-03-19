package com.smartlease.smartlease_backend.service;

import com.smartlease.smartlease_backend.dto.PropertyResponse;
import com.smartlease.smartlease_backend.exception.BadRequestException;
import com.smartlease.smartlease_backend.model.Property;
import com.smartlease.smartlease_backend.model.Role;
import com.smartlease.smartlease_backend.model.User;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import com.smartlease.smartlease_backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

  @Mock
  private PropertyRepository propertyRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private PropertyService propertyService;

  private User ownerUser;
  private User otherUser;
  private Property testProperty;

  @BeforeEach
  void setUp() {
    ownerUser = new User();
    ownerUser.setId(1L);
    ownerUser.setName("Landlord");
    ownerUser.setEmail("landlord@example.com");
    ownerUser.setPassword("password");
    ownerUser.setRole(Role.ROLE_LANDLORD);

    otherUser = new User();
    otherUser.setId(2L);
    otherUser.setName("Other User");
    otherUser.setEmail("other@example.com");
    otherUser.setPassword("password");
    otherUser.setRole(Role.ROLE_TENANT);

    testProperty = Property.builder()
        .id(100L)
        .title("Cozy Apartment")
        .description("A nice place to live")
        .address("123 Main St")
        .price(1500.0)
        .available(true)
        .imageUrl("http://example.com/img.jpg")
        .owner(ownerUser)
        .build();
  }

  @AfterEach
  void tearDown() {
    // Clear the SecurityContext after each test to avoid leaking state
    SecurityContextHolder.clearContext();
  }

  // ===================== GET PROPERTY BY ID =====================

  @Test
    @DisplayName("getPropertyById should return PropertyResponse when property exists")
    void getPropertyById_shouldReturnPropertyResponse() {
        // Arrange
        when(propertyRepository.findById(100L)).thenReturn(Optional.of(testProperty));

        // Act
        PropertyResponse response = propertyService.getPropertyById(100L);

        // Assert
        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Cozy Apartment", response.getTitle());
        assertEquals("123 Main St", response.getAddress());
        assertEquals(1500.0, response.getPrice());
        assertEquals("Landlord", response.getOwnerName());
    }

  @Test
    @DisplayName("getPropertyById should throw BadRequestException when property not found")
    void getPropertyById_shouldThrowExceptionWhenNotFound() {
        // Arrange
        when(propertyRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> propertyService.getPropertyById(999L));
    }

  // ===================== GET ALL PROPERTIES =====================

  @Test
  @DisplayName("getAllProperties should return a list of PropertyResponse objects")
  void getAllProperties_shouldReturnMappedList() {
    // Arrange
    Property property2 = Property.builder()
        .id(101L)
        .title("Studio Flat")
        .description("Small but cozy")
        .address("456 Oak Ave")
        .price(800.0)
        .available(true)
        .imageUrl("http://example.com/img2.jpg")
        .owner(ownerUser)
        .build();

    when(propertyRepository.findAll()).thenReturn(List.of(testProperty, property2));

    // Act
    List<PropertyResponse> responses = propertyService.getAllProperties();

    // Assert
    assertEquals(2, responses.size());
    assertEquals("Cozy Apartment", responses.get(0).getTitle());
    assertEquals("Studio Flat", responses.get(1).getTitle());
  }

  @Test
    @DisplayName("getAllProperties should return empty list when no properties exist")
    void getAllProperties_shouldReturnEmptyListWhenNoProperties() {
        // Arrange
        when(propertyRepository.findAll()).thenReturn(List.of());

        // Act
        List<PropertyResponse> responses = propertyService.getAllProperties();

        // Assert
        assertTrue(responses.isEmpty());
    }

  // ===================== DELETE PROPERTY =====================

  @Test
  @DisplayName("deleteProperty should succeed when the authenticated user is the owner")
  void deleteProperty_shouldSucceedForOwner() {
    // Arrange — set ownerUser as the authenticated user
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(ownerUser, null, ownerUser.getAuthorities()));
    when(propertyRepository.findById(100L)).thenReturn(Optional.of(testProperty));

    // Act
    assertDoesNotThrow(() -> propertyService.deleteProperty(100L));

    // Assert
    verify(propertyRepository, times(1)).delete(testProperty);
  }

  @Test
  @DisplayName("deleteProperty should throw AccessDeniedException when user is not the owner")
  void deleteProperty_shouldThrowAccessDeniedForNonOwner() {
    // Arrange — set otherUser (not the owner) as authenticated user
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(otherUser, null, otherUser.getAuthorities()));
    when(propertyRepository.findById(100L)).thenReturn(Optional.of(testProperty));

    // Act & Assert
    assertThrows(AccessDeniedException.class,
        () -> propertyService.deleteProperty(100L));

    verify(propertyRepository, never()).delete(any());
  }

  @Test
  @DisplayName("deleteProperty should throw BadRequestException when property not found")
  void deleteProperty_shouldThrowExceptionWhenNotFound() {
    // Arrange
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(ownerUser, null, ownerUser.getAuthorities()));
    when(propertyRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(BadRequestException.class,
        () -> propertyService.deleteProperty(999L));
  }
}
