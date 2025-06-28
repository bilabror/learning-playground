package com.quizku.models;

/**
 * Kelas untuk soal True/False
 * Inheritance dari Question class
 */
public class TrueFalseQuestion extends Question {
    private boolean correctAnswer; // true atau false
    
    // Constructor default
    public TrueFalseQuestion() {
        super();
    }
    
    // Constructor dengan parameter
    public TrueFalseQuestion(String questionText, Subject subject, int points, boolean correctAnswer) {
        super(questionText, subject, points);
        this.correctAnswer = correctAnswer;
    }
    
    // Constructor lengkap
    public TrueFalseQuestion(int id, String questionText, Subject subject, int points, boolean correctAnswer) {
        super(id, questionText, subject, points);
        this.correctAnswer = correctAnswer;
    }
    
    // Getters dan Setters
    public void setCorrectAnswer(boolean correctAnswer) { 
        this.correctAnswer = correctAnswer; 
    }
    
    // Implementation abstract methods (Polymorphism)
    @Override
    public boolean checkAnswer(String answer) {
        try {
            boolean userAnswer = Boolean.parseBoolean(answer);
            return userAnswer == correctAnswer;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String getCorrectAnswer() {
        return String.valueOf(correctAnswer);
    }
    
    @Override
    public QuestionType getType() {
        return QuestionType.TRUE_FALSE;
    }
    
    // Method khusus untuk True/False question
    public boolean getCorrectAnswerAsBoolean() {
        return correctAnswer;
    }
} 