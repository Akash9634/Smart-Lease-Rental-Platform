package com.smartlease.smartlease_backend.service;

import com.smartlease.smartlease_backend.dto.BookingResponse;
import com.smartlease.smartlease_backend.exception.BadRequestException;
import com.smartlease.smartlease_backend.model.Booking;
import com.smartlease.smartlease_backend.model.User;
import com.smartlease.smartlease_backend.repository.BookingRepository;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;

    public BookingService(BookingRepository bookingRepository, PropertyRepository propertyRepository){
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
    }

    public BookingResponse createBooking(Long propertyId){
        var property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new BadRequestException("property not found"));

        //check availability of property
        if(!property.isAvailable()){
            throw new RuntimeException("Property is already booked");
        }

        //get the user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        //create the booking
        Booking booking = Booking.builder()
                .user(user)
                .property(property)
                .status("BOOKED")
                .bookingDate(LocalDateTime.now())
                .build();

        bookingRepository.save(booking);

        //update property available property to false
        property.setAvailable(false);
        propertyRepository.save(property);

        //return the BookingResponseDTO
        return BookingResponse.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .bookingDate(booking.getBookingDate())
                .propertyTitle(property.getTitle())
                .propertyId(property.getId())
                .tenantName(user.getName())
                .build();



    }
}
