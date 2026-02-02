package com.smartlease.smartlease_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    //which user booked it;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //which property is booked
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String status; //BOOKED, PENDING or CANCELLED

    private LocalDateTime bookingDate;




}
