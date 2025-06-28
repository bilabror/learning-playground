package com.quizku.models;

/**
 * Kelas untuk soal pilihan ganda
 * Inheritance dari Question class
 */
public class MultipleChoiceQuestion extends Question {
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer; // A, B, C, atau D
    
    // Constructor default
    public MultipleChoiceQuestion() {
        super();
    }
    
    // Constructor dengan parameter
    public MultipleChoiceQuestion(String questionText, Subject subject, int points,
                                 String optionA, String optionB, String optionC, 
                                 String optionD, String correctAnswer) {
        super(questionText, subject, points);
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }
    
    // Constructor lengkap
    public MultipleChoiceQuestion(int id, String questionText, Subject subject, int points,
                                 String optionA, String optionB, String optionC, 
                                 String optionD, String correctAnswer) {
        super(id, questionText, subject, points);
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }
    
    // Getters dan Setters
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    // Implementation abstract methods (Polymorphism)
    @Override
    public boolean checkAnswer(String answer) {
        return correctAnswer != null && correctAnswer.equalsIgnoreCase(answer);
    }
    
    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    @Override
    public QuestionType getType() {
        return QuestionType.MULTIPLE_CHOICE;
    }
    
    // Method untuk mendapatkan opsi berdasarkan huruf
    public String getOptionByLetter(String letter) {
        switch (letter.toUpperCase()) {
            case "A": return optionA;
            case "B": return optionB;
            case "C": return optionC;
            case "D": return optionD;
            default: return "";
        }
    }
    
    @Override
    public boolean isValid() {
        return super.isValid() && 
               optionA != null && !optionA.trim().isEmpty() &&
               optionB != null && !optionB.trim().isEmpty() &&
               optionC != null && !optionC.trim().isEmpty() &&
               optionD != null && !optionD.trim().isEmpty() &&
               correctAnswer != null && correctAnswer.matches("[A-Da-d]");
    }
} 