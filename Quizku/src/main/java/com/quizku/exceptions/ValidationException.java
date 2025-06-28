package com.quizku.exceptions;

/**
 * Custom exception untuk validasi input
 * Menggunakan konsep exception handling
 */
public class ValidationException extends Exception {
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
} 