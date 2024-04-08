package com.example.ShopApp.dtos;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    ADMIN(1),
    USER(2),
    GUEST(3);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public static UserRole fromValue(int value) {
        for (UserRole role : UserRole.values()) {
            if (role.value == value) {
                return role;
            }
        }
        return null;
    }
}
