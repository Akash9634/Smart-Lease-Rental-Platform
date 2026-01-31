package com.smartlease.smartlease_backend.service;

import com.smartlease.smartlease_backend.dto.PropertyRequest;
import com.smartlease.smartlease_backend.model.Property;
import com.smartlease.smartlease_backend.model.User;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import com.smartlease.smartlease_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public void saveProperty(PropertyRequest request, Principal connectedUser){
        //get the currently logged-in user
        var user = (User) ((org.springframework.security.authentication.UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

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

    public void updateProperty(Long propertyId, PropertyRequest request, Principal connectedUser){

        //get the current user
        var User = (User) ((org.springframework.security.authentication.UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        //find the property or else throw error
        var property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("property not found"));

    }



}
