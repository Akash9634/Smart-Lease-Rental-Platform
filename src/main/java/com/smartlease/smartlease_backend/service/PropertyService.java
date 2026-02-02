package com.smartlease.smartlease_backend.service;

import com.smartlease.smartlease_backend.dto.PropertyRequest;
import com.smartlease.smartlease_backend.dto.PropertyResponse;
import com.smartlease.smartlease_backend.exception.BadRequestException;
import com.smartlease.smartlease_backend.model.Property;
import com.smartlease.smartlease_backend.model.User;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import com.smartlease.smartlease_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public PropertyResponse saveProperty(PropertyRequest request){
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

        return PropertyResponse.builder()
                .title(property.getTitle())
                .description(property.getDescription())
                .address(property.getAddress())
                .price(property.getPrice())
                .imageUrl(property.getImageUrl())
                .build();

    }

    @Transactional
    public PropertyResponse updateProperty(Long propertyId, PropertyRequest request){
        //get the current user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        //find the property or else throw error
        var property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new BadRequestException("property not found"));

        if(!Objects.equals(user.getId(), property.getOwner().getId())){
            throw new AccessDeniedException("you are not authorized to perform this operation");
        }

        property.setAddress(request.getAddress());
        property.setPrice(request.getPrice());
        property.setDescription(request.getDescription());
        property.setImageUrl(request.getImageUrl());
        property.setTitle(request.getTitle());

        propertyRepository.save(property);

        return PropertyResponse.builder()
                .title(property.getTitle())
                .description(property.getDescription())
                .address(property.getAddress())
                .price(property.getPrice())
                .imageUrl(property.getImageUrl())
                .build();

    }

    public void deleteProperty(Long propertyId){
        //get the property
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new BadRequestException("Property not found"));

        //get the user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        //check if property belongs to the user
        if(!Objects.equals(user.getId(), property.getOwner().getId())){
            throw new AccessDeniedException("You are not authorized to perform this opeartion");
        }

        //delete the property
        propertyRepository.delete(property);

    }

    public PropertyResponse getPropertyById(Long propertyId){
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new BadRequestException("property not found"));

        PropertyResponse response = PropertyResponse.builder()
                .title(property.getTitle())
                .description(property.getDescription())
                .address(property.getAddress())
                .price(property.getPrice())
                .imageUrl(property.getImageUrl())
                .available(true)
                .build();
        return response;
    }

    public List<PropertyResponse> getMyProperties(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return propertyRepository.findAll()
                .stream()
                .filter(property -> property.getOwner().getId().equals(user.getId()))
                .map(property -> new PropertyResponse(
                        property.getId(),
                        property.getTitle(),
                        property.getDescription(),
                        property.getAddress(),
                        property.getPrice(),
                        property.isAvailable(),
                        property.getImageUrl(),
                        property.getOwner().getName()
                ))
                .toList();
    }



}
