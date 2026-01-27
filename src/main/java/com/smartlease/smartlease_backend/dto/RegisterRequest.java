package com.smartlease.smartlease_backend.dto;


import com.smartlease.smartlease_backend.model.Role;
import lombok.*;

@Getter
@Data
@Builder
public class RegisterRequest { //for registration request
    private String name;
    private String email;
    private String password;
    private Role role;

    public RegisterRequest(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public RegisterRequest() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
