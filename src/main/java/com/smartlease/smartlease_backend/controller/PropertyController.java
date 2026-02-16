package com.smartlease.smartlease_backend.controller;

import com.smartlease.smartlease_backend.dto.PropertyRequest;
import com.smartlease.smartlease_backend.dto.PropertyResponse;
import com.smartlease.smartlease_backend.service.FileUploadService;
import com.smartlease.smartlease_backend.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService service;
    private final FileUploadService fileUploadService;

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_LANDLORD')")
    public ResponseEntity<?> saveProperty(@Valid @RequestPart("data") PropertyRequest request,
                                          @RequestPart("image") MultipartFile image) throws IOException {
//        PropertyResponse response = service.saveProperty(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        //Upload image to Cloudinary
        String imageUrl = fileUploadService.uploadFile(image);

        //set url in the request object
        request.setImageUrl(imageUrl);

        //save property normally
        return ResponseEntity.ok(service.saveProperty(request));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProperty(@RequestBody PropertyRequest request, @PathVariable Long id){
        PropertyResponse response = service.updateProperty(id, request);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id){
        service.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPropertyById(@PathVariable Long id){
        PropertyResponse response = service.getPropertyById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAllProperty(){
         List<PropertyResponse> response = service.getAllProperties();
         return ResponseEntity.ok(response);
    }

    @GetMapping("/my-properties")
    public ResponseEntity<List<PropertyResponse>> getMyProperties(){
        List<PropertyResponse> response = service.getMyProperties();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PropertyResponse>> searchProperties(@RequestParam(required = false) String address,
                                                                   @RequestParam(required = false) Double maxPrice){
        return ResponseEntity.ok(service.searchProperties(address, maxPrice));
    }


}
