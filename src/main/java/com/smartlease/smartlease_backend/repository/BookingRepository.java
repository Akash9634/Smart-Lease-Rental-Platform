package com.smartlease.smartlease_backend.repository;

import com.smartlease.smartlease_backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
