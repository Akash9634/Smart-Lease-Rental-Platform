package com.smartlease.smartlease_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private String status;
    private LocalDateTime bookingDate;
    private String propertyTitle;
    private Long propertyId;
    private String tenantName;
}
