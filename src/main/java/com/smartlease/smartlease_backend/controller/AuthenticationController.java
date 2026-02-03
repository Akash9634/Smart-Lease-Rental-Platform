package com.smartlease.smartlease_backend.controller;

import com.smartlease.smartlease_backend.dto.AuthenticationRequest;
import com.smartlease.smartlease_backend.dto.AuthenticationResponse;
import com.smartlease.smartlease_backend.dto.RegisterRequest;
import com.smartlease.smartlease_backend.dto.UserRequest;
import com.smartlease.smartlease_backend.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.login(request));
    }

    @DeleteMapping("/delete-me")
    public ResponseEntity<?> deleteUser(){
        service.deleteUser();
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest user){
        service.updateUser(user);
        return ResponseEntity.ok("user updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        service.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
