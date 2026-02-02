package com.smartlease.smartlease_backend.controller;

import com.smartlease.smartlease_backend.model.Booking;
import com.smartlease.smartlease_backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping("/{propertyId}")
    public ResponseEntity<Booking> saveBooking(@PathVariable Long propertyId){
        Booking booking = service.createBooking(propertyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }
}
