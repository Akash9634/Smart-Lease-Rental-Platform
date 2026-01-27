package com.smartlease.smartlease_backend.controller;

import com.smartlease.smartlease_backend.dto.PropertyRequest;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import com.smartlease.smartlease_backend.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService service;

    @PostMapping
    public ResponseEntity<?> saveProperty(@RequestBody PropertyRequest request, Principal connectedUser){
        service.saveProperty(request, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("property saved successfully");
    }
}
