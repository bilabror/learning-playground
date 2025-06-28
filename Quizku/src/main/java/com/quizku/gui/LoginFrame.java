package com.quizku.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.quizku.database.DatabaseManager;
import com.quizku.models.User;
import com.quizku.models.UserRole;
import com.quizku.exceptions.DatabaseException;

/**
 * Login Frame untuk autentikasi pengguna
 * Menggunakan Java Swing untuk GUI
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private DatabaseManager dbManager;
    
    public LoginFrame() {
        this.dbManager = DatabaseManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupFrame();
    }
    
    /**
     * Inisialisasi komponen GUI
     */
    private void initializeComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");
        
        // Set button styling
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        signUpButton.setBackground(new Color(34, 139, 34));
        signUpButton.setForeground(Color.BLACK);
        signUpButton.setFocusPainted(false);
    }
    
    /**
     * Setup layout menggunakan GridBagLayout
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel utama
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("QUIZKU");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Aplikasi Kuis Edukatif");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);
        
        // Spacer
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(new JLabel(), gbc);
        
        // Username label dan field
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);
        
        // Password label dan field
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Setup event listeners untuk button
     */
    private void setupEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });
        
        // Enter key untuk login
        getRootPane().setDefaultButton(loginButton);
    }
    
    /**
     * Setup frame properties
     */
    private void setupFrame() {
        setTitle("Quizku - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * Handle login process dengan exception handling
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validasi input
        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Username dan password tidak boleh kosong!");
            return;
        }
        
        try {
            // Coba autentikasi user
            User user = dbManager.authenticateUser(username, password);
            
            if (user != null) {
                showSuccessMessage("Login berhasil! Selamat datang " + user.getUsername());
                
                // Buka main frame berdasarkan role
                SwingUtilities.invokeLater(() -> {
                    if (user.getRole() == UserRole.ADMIN) {
                        new AdminFrame(user).setVisible(true);
                    } else {
                        new StudentFrame(user).setVisible(true);
                    }
                    dispose();
                });
                
            } else {
                showErrorMessage("Username atau password salah!");
                passwordField.setText(""); // Clear password
            }
            
        } catch (DatabaseException ex) {
            showErrorMessage("Error database: " + ex.getMessage());
        } catch (Exception ex) {
            showErrorMessage("Terjadi kesalahan: " + ex.getMessage());
        }
    }
    
    /**
     * Handle sign up process
     */
    private void handleSignUp() {
        SignUpDialog signUpDialog = new SignUpDialog(this);
        signUpDialog.setVisible(true);
    }
    
    /**
     * Show error message dialog
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show success message dialog
     */
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
} 