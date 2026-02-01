package com.smartlease.smartlease_backend.service;

import com.smartlease.smartlease_backend.dto.PropertyRequest;
import com.smartlease.smartlease_backend.model.Property;
import com.smartlease.smartlease_backend.model.User;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import com.smartlease.smartlease_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public void saveProperty(PropertyRequest request){
        //get the currently logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        //create property object
        var property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .address(request.getAddress())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .available(true) //default true
                .owner(user) //link property to landlord
                .build();
        //save to db
        propertyRepository.save(property);

    }

    public void updateProperty(Long propertyId, PropertyRequest request){
        //get the current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        //find the property or else throw error
        var property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("property not found"));

    }



}
