package com.quizku.services;

import com.quizku.interfaces.Scorable;

/**
 * Implementasi interface Scorable untuk menghitung skor kuis
 */
public class ScoreCalculator implements Scorable {
    private static final double PASSING_SCORE = 70.0;
    
    @Override
    public double calculateScore(int correctAnswers, int totalQuestions) {
        if (totalQuestions == 0) {
            return 0.0;
        }
        return ((double) correctAnswers / totalQuestions) * 100.0;
    }
    
    @Override
    public String getGrade(double score) {
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "E";
        }
    }
    
    @Override
    public boolean isPassed(double score) {
        return score >= PASSING_SCORE;
    }
    
    /**
     * Method tambahan untuk mendapatkan pesan hasil
     */
    public String getResultMessage(double score) {
        if (isPassed(score)) {
            return "Selamat! Anda LULUS dengan nilai " + String.format("%.1f", score);
        } else {
            return "Maaf, Anda TIDAK LULUS. Nilai Anda " + String.format("%.1f", score);
        }
    }
} 