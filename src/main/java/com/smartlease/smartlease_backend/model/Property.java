package com.smartlease.smartlease_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "property")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String address;
    private Double price;
    private boolean available;

    private String imageUrl;

    //Relationship: Link this property to the User(Landlord) who posted it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //creates foreign key column in the DB
    private User owner;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;




}
