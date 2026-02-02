package com.smartlease.smartlease_backend.dto;

public class UserRequest {
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
