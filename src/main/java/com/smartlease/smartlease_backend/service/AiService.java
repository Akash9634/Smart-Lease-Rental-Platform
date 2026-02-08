package com.smartlease.smartlease_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateDescription(String keywords){

        //construct apiEndPoint
        String finalUrl = apiUrl + apiKey;

        //create the prompt
        String prompt = "Write professional and attractive , and SEO friendly real estate listing description with these features"
                + keywords + "keep it under 100 words";

        //construct the json request body
        // Structure: { "contents": [{ "parts": [{ "text": "YOUR PROMPT" }] }] }

        Map<String, Object> parts = new HashMap<>();
        parts.put("text", prompt);

        Map<String, Object> contents = new HashMap<>();
        contents.put("parts", List.of(parts)); //creating a list of map parts "contents" = { parts = {    "text": "YOUR PROMPT" } }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(contents));

        //send the request to google gemini api
        try{
            Map response = restTemplate.postForObject(finalUrl, requestBody, Map.class);
            return extractTextFromResponse(response);
        }
        catch(Exception e){
            return "Error generating description: " + e.getMessage();
        }


    }

    // Helper method to dig through the complex JSON response
    private String extractTextFromResponse(Map response) {
        try {
            List<Map> candidates = (List<Map>) response.get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "Failed to parse AI response.";
        }
    }
}
