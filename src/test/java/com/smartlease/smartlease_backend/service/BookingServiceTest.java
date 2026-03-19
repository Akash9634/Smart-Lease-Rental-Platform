package com.smartlease.smartlease_backend.service;

import com.smartlease.smartlease_backend.dto.BookingResponse;
import com.smartlease.smartlease_backend.exception.BadRequestException;
import com.smartlease.smartlease_backend.model.Booking;
import com.smartlease.smartlease_backend.model.Property;
import com.smartlease.smartlease_backend.model.Role;
import com.smartlease.smartlease_backend.model.User;
import com.smartlease.smartlease_backend.repository.BookingRepository;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @Mock
  private BookingRepository bookingRepository;

  @Mock
  private PropertyRepository propertyRepository;

  @InjectMocks
  private BookingService bookingService;

  private User tenantUser;
  private Property availableProperty;
  private Property bookedProperty;

  @BeforeEach
  void setUp() {
    tenantUser = new User();
    tenantUser.setId(1L);
    tenantUser.setName("Tenant");
    tenantUser.setEmail("tenant@example.com");
    tenantUser.setPassword("password");
    tenantUser.setRole(Role.ROLE_TENANT);

    // Set the tenant as the authenticated user
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(tenantUser, null, tenantUser.getAuthorities()));

    User landlord = new User();
    landlord.setId(2L);
    landlord.setName("Landlord");
    landlord.setEmail("landlord@example.com");
    landlord.setPassword("password");
    landlord.setRole(Role.ROLE_LANDLORD);

    availableProperty = Property.builder()
        .id(10L)
        .title("Nice Flat")
        .description("A really nice flat")
        .address("789 Elm St")
        .price(1200.0)
        .available(true)
        .imageUrl("http://example.com/flat.jpg")
        .owner(landlord)
        .build();

    bookedProperty = Property.builder()
        .id(11L)
        .title("Booked Flat")
        .description("Already taken")
        .address("000 Taken St")
        .price(900.0)
        .available(false)
        .imageUrl("http://example.com/booked.jpg")
        .owner(landlord)
        .build();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  // ===================== CREATE BOOKING =====================

  @Test
    @DisplayName("createBooking should succeed for an available property")
    void createBooking_shouldSucceedForAvailableProperty() {
        // Arrange
        when(propertyRepository.findById(10L)).thenReturn(Optional.of(availableProperty));

        // Act
        BookingResponse response = bookingService.createBooking(10L);

        // Assert
        assertNotNull(response);
        assertEquals("BOOKED", response.getStatus());
        assertEquals("Nice Flat", response.getPropertyTitle());
        assertEquals("Tenant", response.getTenantName());
        assertEquals(10L, response.getPropertyId());

        // Verify the property was marked unavailable
        assertFalse(availableProperty.isAvailable());
        verify(propertyRepository, times(1)).save(availableProperty);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

  @Test
    @DisplayName("createBooking should throw RuntimeException when property is already booked")
    void createBooking_shouldThrowExceptionWhenPropertyAlreadyBooked() {
        // Arrange
        when(propertyRepository.findById(11L)).thenReturn(Optional.of(bookedProperty));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(11L));

        assertEquals("Property is already booked", exception.getMessage());
        verify(bookingRepository, never()).save(any());
    }

  @Test
    @DisplayName("createBooking should throw BadRequestException when property not found")
    void createBooking_shouldThrowExceptionWhenPropertyNotFound() {
        // Arrange
        when(propertyRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> bookingService.createBooking(999L));
    }

  // ===================== DELETE BOOKING =====================

  @Test
  @DisplayName("deleteBookingById should succeed when booking exists")
  void deleteBookingById_shouldSucceedWhenBookingExists() {
    // Arrange
    Booking booking = Booking.builder()
        .id(50L)
        .user(tenantUser)
        .property(availableProperty)
        .status("BOOKED")
        .build();
    when(bookingRepository.findById(50L)).thenReturn(Optional.of(booking));

    // Act
    assertDoesNotThrow(() -> bookingService.deleteBookingById(50L));

    // Assert
    verify(bookingRepository, times(1)).delete(booking);
  }

  @Test
    @DisplayName("deleteBookingById should throw BadRequestException when booking not found")
    void deleteBookingById_shouldThrowExceptionWhenNotFound() {
        // Arrange
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class,
                () -> bookingService.deleteBookingById(999L));
    }
}
