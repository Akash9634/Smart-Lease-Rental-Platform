package com.smartlease.smartlease_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequest {
    private String title;
    private String description;
    private String address;
    private Double price;
    private String imageUrl;
}
