package com.smartlease.smartlease_backend.controller;

import com.smartlease.smartlease_backend.dto.PropertyRequest;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import com.smartlease.smartlease_backend.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAuthority;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService service;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('LANDLORD')")
    public ResponseEntity<?> saveProperty(@RequestBody PropertyRequest request, Principal connectedUser){
        service.saveProperty(request, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("property saved successfully");
    }
}
