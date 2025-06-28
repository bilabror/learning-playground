package com.quizku.models;

import java.util.List;
import java.util.ArrayList;

/**
 * Kelas Quiz untuk mengelola kumpulan soal
 */
public class Quiz {
    private int id;
    private String title;
    private Subject subject;
    private QuestionType questionType;
    private List<Question> questions;
    private int timeLimit; // dalam menit
    
    // Constructor default
    public Quiz() {
        this.questions = new ArrayList<>();
    }
    
    // Constructor dengan parameter
    public Quiz(String title, Subject subject, QuestionType questionType, int timeLimit) {
        this.title = title;
        this.subject = subject;
        this.questionType = questionType;
        this.timeLimit = timeLimit;
        this.questions = new ArrayList<>();
    }
    
    // Constructor lengkap
    public Quiz(int id, String title, Subject subject, QuestionType questionType, int timeLimit) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.questionType = questionType;
        this.timeLimit = timeLimit;
        this.questions = new ArrayList<>();
    }
    
    // Getters dan Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    
    public QuestionType getQuestionType() { return questionType; }
    public void setQuestionType(QuestionType questionType) { this.questionType = questionType; }
    
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    
    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }
    
    // Method untuk menambah soal
    public void addQuestion(Question question) {
        if (question != null && question.getType() == this.questionType) {
            this.questions.add(question);
        }
    }
    
    // Method untuk menghapus soal
    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }
    
    // Method untuk mendapatkan jumlah soal
    public int getTotalQuestions() {
        return questions.size();
    }
    
    // Method untuk mendapatkan total poin
    public int getTotalPoints() {
        return questions.stream().mapToInt(Question::getPoints).sum();
    }
    
    // Method validasi
    public boolean isValid() {
        return title != null && !title.trim().isEmpty() &&
               subject != null && questionType != null &&
               timeLimit > 0;
    }
    
    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subject=" + subject +
                ", questionType=" + questionType +
                ", totalQuestions=" + getTotalQuestions() +
                ", timeLimit=" + timeLimit +
                '}';
    }
} 