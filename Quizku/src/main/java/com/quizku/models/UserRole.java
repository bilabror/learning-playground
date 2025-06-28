package com.quizku.models;

/**
 * Enum untuk role/peran user dalam aplikasi
 */
public enum UserRole {
    ADMIN("Admin"),
    STUDENT("Student");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
} 