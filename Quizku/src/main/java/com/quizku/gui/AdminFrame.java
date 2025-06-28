package com.quizku.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.quizku.database.DatabaseManager;
import com.quizku.models.*;

/**
 * Admin Frame untuk mengelola soal (CRUD)
 */
public class AdminFrame extends JFrame {
    private User currentUser;
    private DatabaseManager dbManager;
    
    // GUI Components
    private JTabbedPane tabbedPane;
    private JTable questionTable;
    private DefaultTableModel tableModel;
    private JButton addQuestionButton;
    private JButton editQuestionButton;
    private JButton deleteQuestionButton;
    private JButton refreshButton;
    private JButton logoutButton;
    
    public AdminFrame(User user) {
        this.currentUser = user;
        this.dbManager = DatabaseManager.getInstance();
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupFrame();
        loadQuestions();
    }
    
    /**
     * Inisialisasi komponen
     */
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        // Table untuk menampilkan soal
        String[] columnNames = {"ID", "Soal", "Mata Pelajaran", "Jenis", "Poin"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        questionTable = new JTable(tableModel);
        questionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Buttons
        addQuestionButton = new JButton("Tambah Soal");
        editQuestionButton = new JButton("Edit Soal");
        deleteQuestionButton = new JButton("Hapus Soal");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");
        
        // Button styling
        addQuestionButton.setBackground(new Color(34, 139, 34));
        addQuestionButton.setForeground(Color.BLACK);
        addQuestionButton.setFocusPainted(false);
        
        editQuestionButton.setBackground(new Color(70, 130, 180));
        editQuestionButton.setForeground(Color.BLACK);
        editQuestionButton.setFocusPainted(false);
        
        deleteQuestionButton.setBackground(new Color(220, 20, 60));
        deleteQuestionButton.setForeground(Color.BLACK);
        deleteQuestionButton.setFocusPainted(false);
        
        refreshButton.setBackground(new Color(255, 165, 0));
        refreshButton.setForeground(Color.BLACK);
        refreshButton.setFocusPainted(false);
        
        logoutButton.setBackground(new Color(128, 128, 128));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);
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
        
        JLabel titleLabel = new JLabel("QUIZKU - Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Admin: " + currentUser.getUsername());
        userLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        userLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        // Question Management Tab
        JPanel questionPanel = createQuestionManagementPanel();
        tabbedPane.addTab("Kelola Soal", questionPanel);
        
        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(logoutButton);
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create question management panel
     */
    private JPanel createQuestionManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Table panel
        JScrollPane tableScrollPane = new JScrollPane(questionTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Soal"));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addQuestionButton);
        buttonPanel.add(editQuestionButton);
        buttonPanel.add(deleteQuestionButton);
        buttonPanel.add(refreshButton);
        
        panel.add(tableScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        addQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddQuestionDialog();
            }
        });
        
        editQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedQuestion();
            }
        });
        
        deleteQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedQuestion();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadQuestions();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });
    }
    
    /**
     * Setup frame properties
     */
    private void setupFrame() {
        setTitle("Quizku - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
    
    /**
     * Load questions from database
     */
    private void loadQuestions() {
        try {
            tableModel.setRowCount(0); // Clear existing data
            
            // Load questions for each subject and type
            for (Subject subject : Subject.values()) {
                for (QuestionType type : QuestionType.values()) {
                    List<Question> questions = dbManager.getQuestions(subject, type);
                    for (Question question : questions) {
                        Object[] row = {
                            question.getId(),
                            truncateText(question.getQuestionText(), 50),
                            question.getSubject().getDisplayName(),
                            question.getType().getDisplayName(),
                            question.getPoints()
                        };
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (Exception e) {
            showErrorMessage("Error loading questions: " + e.getMessage());
        }
    }
    
    /**
     * Show add question dialog
     */
    private void showAddQuestionDialog() {
        AddQuestionDialog dialog = new AddQuestionDialog(this, dbManager);
        dialog.setVisible(true);
        loadQuestions(); // Refresh table
    }
    
    /**
     * Edit selected question
     */
    private void editSelectedQuestion() {
        int selectedRow = questionTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Pilih soal yang ingin diedit!");
            return;
        }
        
        showInfoMessage("Fitur edit belum diimplementasi. Gunakan hapus lalu tambah baru.");
    }
    
    /**
     * Delete selected question
     */
    private void deleteSelectedQuestion() {
        int selectedRow = questionTable.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Pilih soal yang ingin dihapus!");
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus soal ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            // Implementation untuk delete akan ditambahkan kemudian
            showInfoMessage("Fitur hapus belum diimplementasi.");
        }
    }
    
    /**
     * Handle logout
     */
    private void handleLogout() {
        int option = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    /**
     * Truncate text untuk display di table
     */
    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
    
    /**
     * Show error message
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show info message
     */
    private void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
} 