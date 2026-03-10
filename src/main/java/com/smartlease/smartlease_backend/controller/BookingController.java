package com.smartlease.smartlease_backend.controller;

import com.smartlease.smartlease_backend.dto.BookingResponse;
import com.smartlease.smartlease_backend.model.Booking;
import com.smartlease.smartlease_backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping("/{propertyId}")
    public ResponseEntity<BookingResponse> saveBooking(@PathVariable Long propertyId){
        BookingResponse booking = service.createBooking(propertyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BookingResponse>> getAllBookings(@PathVariable Long userId){
        List<BookingResponse> bookings = service.getAllBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBookingById(@PathVariable Long bookingId){
        service.deleteBookingById(bookingId);
        return  ResponseEntity.ok("booking deleted successfully");
    }
}
