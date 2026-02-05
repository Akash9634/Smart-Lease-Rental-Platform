package com.smartlease.smartlease_backend.repository;

import com.smartlease.smartlease_backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {

     List<ChatMessage> findByPropertyId(Long propertyId);
}
