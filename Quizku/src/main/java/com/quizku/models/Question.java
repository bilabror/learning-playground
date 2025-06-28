package com.quizku.models;

/**
 * Abstract class Question sebagai parent class untuk semua jenis soal
 * Menggunakan konsep inheritance dan polymorphism
 */
public abstract class Question {
    protected int id;
    protected String questionText;
    protected Subject subject;
    protected int points;
    
    // Constructor default
    public Question() {}
    
    // Constructor dengan parameter
    public Question(String questionText, Subject subject, int points) {
        this.questionText = questionText;
        this.subject = subject;
        this.points = points;
    }
    
    // Constructor lengkap
    public Question(int id, String questionText, Subject subject, int points) {
        this.id = id;
        this.questionText = questionText;
        this.subject = subject;
        this.points = points;
    }
    
    // Getters dan Setters (Encapsulation)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    
    // Abstract method untuk polymorphism
    public abstract boolean checkAnswer(String answer);
    public abstract String getCorrectAnswer();
    public abstract QuestionType getType();
    
    // Method untuk validasi
    public boolean isValid() {
        return questionText != null && !questionText.trim().isEmpty() &&
               subject != null && points > 0;
    }
    
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", subject=" + subject +
                ", points=" + points +
                '}';
    }
} 