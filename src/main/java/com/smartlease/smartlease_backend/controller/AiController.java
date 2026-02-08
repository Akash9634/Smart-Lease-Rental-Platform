package com.smartlease.smartlease_backend.controller;

import com.smartlease.smartlease_backend.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    @GetMapping("/generate")
    public ResponseEntity<Map<String, String>> generateDescription(@RequestParam(defaultValue = "beautiful house and great facilities") String keywords){
        String description = aiService.generateDescription(keywords);

        //return json: { "result": "Discover your dream home..." }
        return ResponseEntity.ok(Map.of("result", description));
    }
}
