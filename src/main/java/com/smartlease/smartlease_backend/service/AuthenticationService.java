package com.smartlease.smartlease_backend.service;


import com.smartlease.smartlease_backend.config.JwtService;
import com.smartlease.smartlease_backend.dto.*;
import com.smartlease.smartlease_backend.model.Booking;
import com.smartlease.smartlease_backend.model.Property;
import com.smartlease.smartlease_backend.model.Role;
import com.smartlease.smartlease_backend.model.User;
import com.smartlease.smartlease_backend.repository.PropertyRepository;
import com.smartlease.smartlease_backend.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PropertyRepository propertyRepository;

    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, PropertyRepository propertyRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.propertyRepository = propertyRepository;
    }

    //register a new user
    public AuthenticationResponse register(RegisterRequest request){

        //check if user already present
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }


        var user = new User(); // we are using var - new feature in java it will look on the right side and figure out itself the type
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());


        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //Authentication - login an existing user
    public AuthenticationResponse login(AuthenticationRequest request){


        //this will verify email/password
        authenticationManager.authenticate(  //authenticate throws exception if not authenticated
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        //if we get here , the user is valid
        var user = repository.findByEmail(request.getEmail()).orElseThrow();// throw generic error
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public void deleteUser(){
        //this user fetched for the transaction which closed immediately
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        //RE-FETCH the user (This attaches it to the current session)
        User user = repository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        //free up the rooms booked by this user
        for(Booking booking : user.getBookings()){
            Property property = booking.getProperty();
            property.setAvailable(true);
            propertyRepository.save(property);
        }
        repository.delete(user);

    }

    public void deleteUserById(Long userId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        //RE-FETCH the user (This attaches it to the current session)
        User user = repository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getRole()!= Role.ROLE_ADMIN){
            throw new AccessDeniedException("You are not authorized to perform this operation");
        }

        for(Booking booking : user.getBookings()){
            Property property = booking.getProperty();
            property.setAvailable(true);
            propertyRepository.save(property);
        }

        repository.deleteById(userId);
    }

    @Transactional
    public void updateUser(UserRequest updateRequest){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if(updateRequest.getName() != null) {
            user.setName(updateRequest.getName());
        }
        repository.save(user);

    }




}

