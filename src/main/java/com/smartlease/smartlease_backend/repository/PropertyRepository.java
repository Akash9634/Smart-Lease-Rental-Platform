package com.smartlease.smartlease_backend.repository;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.smartlease.smartlease_backend.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findAllByOwnerId(Long userId);
}
