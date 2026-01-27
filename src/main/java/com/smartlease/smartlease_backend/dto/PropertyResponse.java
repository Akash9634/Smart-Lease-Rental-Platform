package com.smartlease.smartlease_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    private Long id;
    private String title;
    private String description;
    private String address;
    private Double price;
    private boolean available;
    private String imageUrl;
    private String ownerName;
}
