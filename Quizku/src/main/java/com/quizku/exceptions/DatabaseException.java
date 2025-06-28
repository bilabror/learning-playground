package com.quizku.exceptions;

/**
 * Custom exception untuk operasi database
 * Menggunakan konsep exception handling
 */
public class DatabaseException extends RuntimeException {
    
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
} 