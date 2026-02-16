package com.smartlease.smartlease_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    @NotBlank(message = "Name cannot be blank")
    String name;

    public UserRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
