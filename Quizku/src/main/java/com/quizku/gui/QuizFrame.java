package com.quizku.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.quizku.models.*;
import com.quizku.interfaces.Scorable;

/**
 * Frame untuk menampilkan kuis dan menerima jawaban
 */
public class QuizFrame extends JFrame {
    private User currentUser;
    private List<Question> questions;
    private Subject subject;
    private QuestionType questionType;
    private Scorable scoreCalculator;
    
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private String[] userAnswers;
    
    // GUI Components
    private JLabel questionLabel;
    private JLabel questionNumberLabel;
    private JPanel answerPanel;
    private ButtonGroup answerGroup;
    private JButton nextButton;
    private JButton prevButton;
    private JButton submitButton;
    private JProgressBar progressBar;
    
    public QuizFrame(User user, List<Question> questions, Subject subject, 
                    QuestionType questionType, Scorable scoreCalculator) {
        this.currentUser = user;
        this.questions = questions;
        this.subject = subject;
        this.questionType = questionType;
        this.scoreCalculator = scoreCalculator;
        this.userAnswers = new String[questions.size()];
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupFrame();
        
        // Show first question
        displayQuestion();
    }
    
    /**
     * Inisialisasi komponen
     */
    private void initializeComponents() {
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        answerPanel = new JPanel();
        answerGroup = new ButtonGroup();
        
        nextButton = new JButton("Next >");
        prevButton = new JButton("< Previous");
        submitButton = new JButton("Submit Quiz");
        
        // Button styling
        nextButton.setBackground(new Color(70, 130, 180));
        nextButton.setForeground(Color.BLACK);
        nextButton.setFocusPainted(false);
        
        prevButton.setBackground(new Color(70, 130, 180));
        prevButton.setForeground(Color.BLACK);
        prevButton.setFocusPainted(false);
        
        submitButton.setBackground(new Color(34, 139, 34));
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        
        progressBar = new JProgressBar(0, questions.size());
        progressBar.setStringPainted(true);
        progressBar.setString("0 / " + questions.size());
    }
    
    /**
     * Setup layout
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("KUIS: " + subject.getDisplayName() + " - " + questionType.getDisplayName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("User: " + currentUser.getUsername());
        userLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        userLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        // Progress Panel
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        progressPanel.add(questionNumberLabel, BorderLayout.WEST);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);
        
        // Question Panel
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createTitledBorder("Soal"));
        questionPanel.add(questionLabel, BorderLayout.CENTER);
        
        // Answer Panel
        answerPanel.setBackground(Color.WHITE);
        answerPanel.setBorder(BorderFactory.createTitledBorder("Pilih Jawaban"));
        
        mainPanel.add(questionPanel, BorderLayout.NORTH);
        mainPanel.add(answerPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(submitButton);
        
        // Add to frame
        add(headerPanel, BorderLayout.NORTH);
        add(progressPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer();
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion();
                }
            }
        });
        
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCurrentAnswer();
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    displayQuestion();
                }
            }
        });
        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitQuiz();
            }
        });
    }
    
    /**
     * Setup frame
     */
    private void setupFrame() {
        setTitle("Quizku - Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
    
    /**
     * Display current question
     */
    private void displayQuestion() {
        Question currentQuestion = questions.get(currentQuestionIndex);
        
        // Update question number and progress
        questionNumberLabel.setText("Soal " + (currentQuestionIndex + 1) + " dari " + questions.size());
        progressBar.setValue(currentQuestionIndex + 1);
        progressBar.setString((currentQuestionIndex + 1) + " / " + questions.size());
        
        // Update question text
        questionLabel.setText("<html><body style='width: 600px'>" + currentQuestion.getQuestionText() + "</body></html>");
        
        // Clear previous answer options
        answerPanel.removeAll();
        for (AbstractButton button : java.util.Collections.list(answerGroup.getElements())) {
            answerGroup.remove(button);
        }
        
        // Create answer options based on question type
        if (currentQuestion instanceof MultipleChoiceQuestion mcq) {
            answerPanel.setLayout(new GridLayout(4, 1, 5, 5));
            
            JRadioButton optionA = new JRadioButton("A. " + mcq.getOptionA());
            JRadioButton optionB = new JRadioButton("B. " + mcq.getOptionB());
            JRadioButton optionC = new JRadioButton("C. " + mcq.getOptionC());
            JRadioButton optionD = new JRadioButton("D. " + mcq.getOptionD());
            
            optionA.setActionCommand("A");
            optionB.setActionCommand("B");
            optionC.setActionCommand("C");
            optionD.setActionCommand("D");
            
            answerGroup.add(optionA);
            answerGroup.add(optionB);
            answerGroup.add(optionC);
            answerGroup.add(optionD);
            
            answerPanel.add(optionA);
            answerPanel.add(optionB);
            answerPanel.add(optionC);
            answerPanel.add(optionD);
            
        } else if (currentQuestion instanceof TrueFalseQuestion) {
            answerPanel.setLayout(new GridLayout(2, 1, 5, 5));
            
            JRadioButton trueOption = new JRadioButton("Benar");
            JRadioButton falseOption = new JRadioButton("Salah");
            
            trueOption.setActionCommand("true");
            falseOption.setActionCommand("false");
            
            answerGroup.add(trueOption);
            answerGroup.add(falseOption);
            
            answerPanel.add(trueOption);
            answerPanel.add(falseOption);
        }
        
        // Restore previous answer if exists
        if (userAnswers[currentQuestionIndex] != null) {
            for (AbstractButton button : java.util.Collections.list(answerGroup.getElements())) {
                if (button.getActionCommand().equals(userAnswers[currentQuestionIndex])) {
                    button.setSelected(true);
                    break;
                }
            }
        }
        
        // Update button states
        prevButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < questions.size() - 1);
        submitButton.setEnabled(true);
        
        // Refresh panel
        answerPanel.revalidate();
        answerPanel.repaint();
    }
    
    /**
     * Save current answer
     */
    private void saveCurrentAnswer() {
        ButtonModel selectedButton = answerGroup.getSelection();
        if (selectedButton != null) {
            userAnswers[currentQuestionIndex] = selectedButton.getActionCommand();
        }
    }
    
    /**
     * Submit quiz and show results
     */
    private void submitQuiz() {
        // Save current answer
        saveCurrentAnswer();
        
        // Confirm submit
        int option = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin submit kuis?",
            "Konfirmasi Submit",
            JOptionPane.YES_NO_OPTION);
            
        if (option != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Calculate results
        calculateResults();
        
        // Show results
        showResults();
    }
    
    /**
     * Calculate quiz results
     */
    private void calculateResults() {
        correctAnswers = 0;
        
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String userAnswer = userAnswers[i];
            
            if (userAnswer != null && question.checkAnswer(userAnswer)) {
                correctAnswers++;
            }
        }
    }
    
    /**
     * Show quiz results
     */
    private void showResults() {
        double score = scoreCalculator.calculateScore(correctAnswers, questions.size());
        String grade = scoreCalculator.getGrade(score);
        boolean passed = scoreCalculator.isPassed(score);
        
        String resultMessage = String.format(
            "=== HASIL KUIS ===\n\n" +
            "Mata Pelajaran: %s\n" +
            "Jenis Kuis: %s\n" +
            "Jawaban Benar: %d dari %d\n" +
            "Skor: %.1f\n" +
            "Grade: %s\n" +
            "Status: %s\n\n" +
            "Terima kasih telah mengikuti kuis!",
            subject.getDisplayName(),
            questionType.getDisplayName(),
            correctAnswers,
            questions.size(),
            score,
            grade,
            passed ? "LULUS" : "TIDAK LULUS"
        );
        
        JOptionPane.showMessageDialog(this, resultMessage, "Hasil Kuis", JOptionPane.INFORMATION_MESSAGE);
        
        // Return to student dashboard
        dispose();
        new StudentFrame(currentUser).setVisible(true);
    }
} 