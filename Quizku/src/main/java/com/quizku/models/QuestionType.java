package com.quizku.models;

/**
 * Enum untuk jenis soal dalam aplikasi
 */
public enum QuestionType {
    MULTIPLE_CHOICE("Pilihan Ganda"),
    TRUE_FALSE("Benar/Salah");
    
    private final String displayName;
    
    QuestionType(String displayName) {
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