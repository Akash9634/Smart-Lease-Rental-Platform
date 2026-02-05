package com.smartlease.smartlease_backend.controller;

import com.smartlease.smartlease_backend.model.ChatMessage;
import com.smartlease.smartlease_backend.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;

    //sending messages
    //user sends to /app/chat/{propertyId}
    @MessageMapping("/chat/{propertyId}")
    @SendTo("/topic/property/{propertyId}")  //broadcast to everyone in that room
    public ChatMessage sendMessage(@DestinationVariable Long propertId,
                                   ChatMessage message){
        //set server side data
        message.setPropertyId(propertId);
        message.setTimeStamp(LocalDateTime.now());

        //save to DB
        return chatMessageRepository.save(message);

    }

    // FETCHING HISTORY (Standard REST API)
    // Frontend calls this when opening the chat window to load old messages
    @GetMapping("/api/v1/messages/{propertyId}")
    @ResponseBody // Needed because this is a standard REST call inside a @Controller
    public List<ChatMessage> getChatHistory(@PathVariable Long propertyId) {
        return chatMessageRepository.findByPropertyId(propertyId);
    }
}
