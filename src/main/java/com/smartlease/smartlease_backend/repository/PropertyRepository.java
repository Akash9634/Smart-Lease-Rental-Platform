package com.smartlease.smartlease_backend.repository;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.smartlease.smartlease_backend.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findAllByOwnerId(Long userId);

    @Query("Select p from Property p where" +
            "(:address IS NULL OR LOWER(p.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)"
    )
    List<Property> searchProperties(@Param("address") String address, @Param("maxPrice") Double maxPrice);
}
