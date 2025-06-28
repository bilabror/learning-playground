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
 * Dialog untuk registrasi pengguna baru
 */
public class SignUpDialog extends JDialog {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<UserRole> roleComboBox;
    private JButton signUpButton;
    private JButton cancelButton;
    private DatabaseManager dbManager;
    
    public SignUpDialog(JFrame parent) {
        super(parent, "Sign Up - Quizku", true);
        this.dbManager = DatabaseManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupDialog();
    }
    
    /**
     * Inisialisasi komponen
     */
    private void initializeComponents() {
        usernameField = new JTextField(15);
        emailField = new JTextField(15);
        passwordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);
        
        // ComboBox untuk role (hanya STUDENT yang bisa register sendiri)
        roleComboBox = new JComboBox<>(new UserRole[]{UserRole.STUDENT});
        roleComboBox.setSelectedItem(UserRole.STUDENT);
        
        signUpButton = new JButton("Sign Up");
        cancelButton = new JButton("Cancel");
        
        // Styling buttons
        signUpButton.setBackground(new Color(34, 139, 34));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        
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
        JLabel titleLabel = new JLabel("Daftar Akun Baru");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(34, 139, 34));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // Form fields
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        
        // Username
        gbc.gridy = 1; gbc.gridx = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);
        
        // Email
        gbc.gridy = 2; gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(emailField, gbc);
        
        // Password
        gbc.gridy = 3; gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridy = 4; gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(confirmPasswordField, gbc);
        
        // Role
        gbc.gridy = 5; gbc.gridx = 0; gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(roleComboBox, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(signUpButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        getRootPane().setDefaultButton(signUpButton);
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
     * Handle sign up process
     */
    private void handleSignUp() {
        try {
            // Validasi input
            validateInput();
            
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            UserRole role = (UserRole) roleComboBox.getSelectedItem();
            
            // Cek apakah username sudah ada
            if (dbManager.userExists(username)) {
                showErrorMessage("Username sudah digunakan!");
                return;
            }
            
            // Buat user baru
            User newUser = new User(username, password, email, role);
            
            // Simpan ke database
            if (dbManager.insertUser(newUser)) {
                showSuccessMessage("Registrasi berhasil! Silakan login dengan akun baru Anda.");
                dispose();
            } else {
                showErrorMessage("Gagal menyimpan data user!");
            }
            
        } catch (Exception ex) {
            showErrorMessage("Error: " + ex.getMessage());
        }
    }
    
    /**
     * Validasi input user
     */
    private void validateInput() throws Exception {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (username.isEmpty()) {
            throw new Exception("Username tidak boleh kosong!");
        }
        
        if (username.length() < 3) {
            throw new Exception("Username minimal 3 karakter!");
        }
        
        if (email.isEmpty()) {
            throw new Exception("Email tidak boleh kosong!");
        }
        
        if (!email.contains("@")) {
            throw new Exception("Format email tidak valid!");
        }
        
        if (password.isEmpty()) {
            throw new Exception("Password tidak boleh kosong!");
        }
        
        if (password.length() < 6) {
            throw new Exception("Password minimal 6 karakter!");
        }
        
        if (!password.equals(confirmPassword)) {
            throw new Exception("Konfirmasi password tidak cocok!");
        }
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