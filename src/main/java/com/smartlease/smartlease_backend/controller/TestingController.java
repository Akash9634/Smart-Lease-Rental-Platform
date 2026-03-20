package com.smartlease.smartlease_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingController {


        @GetMapping("/")
        public String home() {
            return "Backend is running 🚀";
        }

}
