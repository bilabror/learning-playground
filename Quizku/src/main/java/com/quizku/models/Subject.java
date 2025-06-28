package com.quizku.models;

/**
 * Enum untuk mata pelajaran dalam aplikasi
 */
public enum Subject {
    MATEMATIKA("Matematika"),
    BAHASA_INDONESIA("Bahasa Indonesia"),
    BAHASA_INGGRIS("Bahasa Inggris"),
    IPA("IPA"),
    IPS("IPS"),
    SEJARAH("Sejarah"),
    GEOGRAFI("Geografi"),
    FISIKA("Fisika"),
    KIMIA("Kimia"),
    BIOLOGI("Biologi");
    
    private final String displayName;
    
    Subject(String displayName) {
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