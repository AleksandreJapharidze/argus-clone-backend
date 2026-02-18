package com.example.argusclone.events.eventclasses;

import java.util.UUID;

public class UserCreationEvent {
    private UUID uuid;
    private String username;
    private String password;
    private String role;

    public UserCreationEvent(UUID uuid, String username, String password, String role) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
