package com.busbooking.busbooking.model;

public enum UserRole {
    ADMIN("Admin - System Manager"),
    USER("User - Passenger"),
    BUS_OPERATOR("Bus Operator - Bus Handler");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return USER; // Default to USER if invalid
        }
    }
}
