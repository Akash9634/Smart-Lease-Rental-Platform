package com.smartlease.smartlease_backend.repository;

import com.smartlease.smartlease_backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
     ChatMessage findByPropertId(Long propertyId);
}
