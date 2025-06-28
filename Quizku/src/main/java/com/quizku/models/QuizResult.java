package com.quizku.models;

import java.time.LocalDateTime;

/**
 * Kelas untuk menyimpan hasil kuis pengguna
 */
public class QuizResult {
    private int id;
    private int userId;
    private int quizId;
    private int correctAnswers;
    private int totalQuestions;
    private double score;
    private String grade;
    private boolean passed;
    private LocalDateTime completedAt;
    
    // Constructor default
    public QuizResult() {
        this.completedAt = LocalDateTime.now();
    }
    
    // Constructor dengan parameter
    public QuizResult(int userId, int quizId, int correctAnswers, int totalQuestions, 
                     double score, String grade, boolean passed) {
        this.userId = userId;
        this.quizId = quizId;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.score = score;
        this.grade = grade;
        this.passed = passed;
        this.completedAt = LocalDateTime.now();
    }
    
    // Constructor lengkap
    public QuizResult(int id, int userId, int quizId, int correctAnswers, int totalQuestions,
                     double score, String grade, boolean passed, LocalDateTime completedAt) {
        this.id = id;
        this.userId = userId;
        this.quizId = quizId;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.score = score;
        this.grade = grade;
        this.passed = passed;
        this.completedAt = completedAt;
    }
    
    // Getters dan Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }
    
    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
    
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    // Method untuk mendapatkan persentase jawaban benar
    public double getPercentage() {
        if (totalQuestions == 0) return 0.0;
        return ((double) correctAnswers / totalQuestions) * 100.0;
    }
    
    @Override
    public String toString() {
        return "QuizResult{" +
                "id=" + id +
                ", userId=" + userId +
                ", quizId=" + quizId +
                ", correctAnswers=" + correctAnswers +
                ", totalQuestions=" + totalQuestions +
                ", score=" + score +
                ", grade='" + grade + '\'' +
                ", passed=" + passed +
                ", completedAt=" + completedAt +
                '}';
    }
} 