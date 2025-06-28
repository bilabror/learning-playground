package com.quizku.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.quizku.database.DatabaseManager;
import com.quizku.models.*;

/**
 * Dialog untuk menambah soal baru
 */
public class AddQuestionDialog extends JDialog {
    private DatabaseManager dbManager;
    
    // Form components
    private JTextArea questionTextArea;
    private JComboBox<Subject> subjectComboBox;
    private JComboBox<QuestionType> typeComboBox;
    private JSpinner pointsSpinner;
    
    // Multiple choice components
    private JTextField optionAField;
    private JTextField optionBField;
    private JTextField optionCField;
    private JTextField optionDField;
    private JComboBox<String> mcCorrectAnswerCombo;
    
    // True/False components
    private JComboBox<String> tfCorrectAnswerCombo;
    
    private JPanel dynamicPanel;
    private JButton saveButton;
    private JButton cancelButton;
    
    public AddQuestionDialog(JFrame parent, DatabaseManager dbManager) {
        super(parent, "Tambah Soal Baru", true);
        this.dbManager = dbManager;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupDialog();
    }
    
    /**
     * Inisialisasi komponen
     */
    private void initializeComponents() {
        questionTextArea = new JTextArea(5, 30);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        
        subjectComboBox = new JComboBox<>(Subject.values());
        typeComboBox = new JComboBox<>(QuestionType.values());
        pointsSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        
        // Multiple choice components
        optionAField = new JTextField(20);
        optionBField = new JTextField(20);
        optionCField = new JTextField(20);
        optionDField = new JTextField(20);
        mcCorrectAnswerCombo = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        
        // True/False components
        tfCorrectAnswerCombo = new JComboBox<>(new String[]{"true", "false"});
        
        dynamicPanel = new JPanel();
        
        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");
        
        // Button styling
        saveButton.setBackground(new Color(34, 139, 34));
        saveButton.setForeground(Color.BLACK);
        saveButton.setFocusPainted(false);
        
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.setFocusPainted(false);
    }
    
    /**
     * Setup layout
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel titleLabel = new JLabel("Tambah Soal Baru");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // Form fields
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        
        // Subject
        gbc.gridy = 1; gbc.gridx = 0;
        mainPanel.add(new JLabel("Mata Pelajaran:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(subjectComboBox, gbc);
        
        // Question Type
        gbc.gridy = 2; gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Jenis Soal:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(typeComboBox, gbc);
        
        // Points
        gbc.gridy = 3; gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Poin:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(pointsSpinner, gbc);
        
        // Question Text
        gbc.gridy = 4; gbc.gridx = 0; gbc.anchor = GridBagConstraints.NORTHEAST;
        mainPanel.add(new JLabel("Soal:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        JScrollPane questionScrollPane = new JScrollPane(questionTextArea);
        mainPanel.add(questionScrollPane, gbc);
        
        // Dynamic panel for question type specific fields
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(dynamicPanel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(15, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Initially show multiple choice options
        showMultipleChoiceOptions();
    }
    
    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        typeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuestionType selectedType = (QuestionType) typeComboBox.getSelectedItem();
                if (selectedType == QuestionType.MULTIPLE_CHOICE) {
                    showMultipleChoiceOptions();
                } else {
                    showTrueFalseOptions();
                }
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveQuestion();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * Setup dialog properties
     */
    private void setupDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
    }
    
    /**
     * Show multiple choice options
     */
    private void showMultipleChoiceOptions() {
        dynamicPanel.removeAll();
        dynamicPanel.setLayout(new GridBagLayout());
        dynamicPanel.setBorder(BorderFactory.createTitledBorder("Opsi Pilihan Ganda"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Option A
        gbc.gridy = 0; gbc.gridx = 0;
        dynamicPanel.add(new JLabel("A:"), gbc);
        gbc.gridx = 1;
        dynamicPanel.add(optionAField, gbc);
        
        // Option B
        gbc.gridy = 1; gbc.gridx = 0;
        dynamicPanel.add(new JLabel("B:"), gbc);
        gbc.gridx = 1;
        dynamicPanel.add(optionBField, gbc);
        
        // Option C
        gbc.gridy = 2; gbc.gridx = 0;
        dynamicPanel.add(new JLabel("C:"), gbc);
        gbc.gridx = 1;
        dynamicPanel.add(optionCField, gbc);
        
        // Option D
        gbc.gridy = 3; gbc.gridx = 0;
        dynamicPanel.add(new JLabel("D:"), gbc);
        gbc.gridx = 1;
        dynamicPanel.add(optionDField, gbc);
        
        // Correct Answer
        gbc.gridy = 4; gbc.gridx = 0;
        dynamicPanel.add(new JLabel("Jawaban Benar:"), gbc);
        gbc.gridx = 1;
        dynamicPanel.add(mcCorrectAnswerCombo, gbc);
        
        dynamicPanel.revalidate();
        dynamicPanel.repaint();
        pack();
    }
    
    /**
     * Show true/false options
     */
    private void showTrueFalseOptions() {
        dynamicPanel.removeAll();
        dynamicPanel.setLayout(new GridBagLayout());
        dynamicPanel.setBorder(BorderFactory.createTitledBorder("Opsi Benar/Salah"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridy = 0; gbc.gridx = 0;
        dynamicPanel.add(new JLabel("Jawaban Benar:"), gbc);
        gbc.gridx = 1;
        dynamicPanel.add(tfCorrectAnswerCombo, gbc);
        
        dynamicPanel.revalidate();
        dynamicPanel.repaint();
        pack();
    }
    
    /**
     * Save question to database
     */
    private void saveQuestion() {
        try {
            // Validasi input
            validateInput();
            
            String questionText = questionTextArea.getText().trim();
            Subject subject = (Subject) subjectComboBox.getSelectedItem();
            QuestionType type = (QuestionType) typeComboBox.getSelectedItem();
            int points = (Integer) pointsSpinner.getValue();
            
            Question question = null;
            
            if (type == QuestionType.MULTIPLE_CHOICE) {
                String optionA = optionAField.getText().trim();
                String optionB = optionBField.getText().trim();
                String optionC = optionCField.getText().trim();
                String optionD = optionDField.getText().trim();
                String correctAnswer = (String) mcCorrectAnswerCombo.getSelectedItem();
                
                question = new MultipleChoiceQuestion(questionText, subject, points,
                                                    optionA, optionB, optionC, optionD, correctAnswer);
            } else {
                String correctAnswerStr = (String) tfCorrectAnswerCombo.getSelectedItem();
                boolean correctAnswer = Boolean.parseBoolean(correctAnswerStr);
                
                question = new TrueFalseQuestion(questionText, subject, points, correctAnswer);
            }
            
            // Simpan ke database
            if (dbManager.insertQuestion(question)) {
                showSuccessMessage("Soal berhasil disimpan!");
                clearForm();
                dispose();
            } else {
                showErrorMessage("Gagal menyimpan soal!");
            }
            
        } catch (Exception ex) {
            showErrorMessage("Error: " + ex.getMessage());
        }
    }
    
    /**
     * Validasi input
     */
    private void validateInput() throws Exception {
        String questionText = questionTextArea.getText().trim();
        if (questionText.isEmpty()) {
            throw new Exception("Teks soal tidak boleh kosong!");
        }
        
        QuestionType type = (QuestionType) typeComboBox.getSelectedItem();
        
        if (type == QuestionType.MULTIPLE_CHOICE) {
            if (optionAField.getText().trim().isEmpty() ||
                optionBField.getText().trim().isEmpty() ||
                optionCField.getText().trim().isEmpty() ||
                optionDField.getText().trim().isEmpty()) {
                throw new Exception("Semua opsi pilihan ganda harus diisi!");
            }
        }
    }
    
    /**
     * Clear form
     */
    private void clearForm() {
        questionTextArea.setText("");
        optionAField.setText("");
        optionBField.setText("");
        optionCField.setText("");
        optionDField.setText("");
        mcCorrectAnswerCombo.setSelectedIndex(0);
        tfCorrectAnswerCombo.setSelectedIndex(0);
        pointsSpinner.setValue(10);
    }
    
    /**
     * Show error message
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show success message
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
} 