package com.quizku;

import com.quizku.gui.LoginFrame;
import com.quizku.database.DatabaseManager;
import javax.swing.*;

/**
 * Main class untuk aplikasi Quizku
 * Entry point aplikasi e-education
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Set look and feel untuk UI yang lebih baik
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            System.out.println("Starting Quizku Application...");
            System.out.println("Initializing database...");
            
            // Inisialisasi database
            DatabaseManager dbManager = DatabaseManager.getInstance();
            dbManager.initializeDatabase();
            
            System.out.println("Database initialized successfully!");
            System.out.println("Starting GUI...");
            
            // Jalankan aplikasi di Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
            
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | 
                 InstantiationException | IllegalAccessException e) {
            System.err.println("Failed to set look and feel: " + e.getMessage());
            // Jalankan dengan default look and feel
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        } catch (Exception e) {
            System.err.println("FATAL ERROR: " + e.getMessage());
            e.printStackTrace();
            
            // Show error dialog to user
            JOptionPane.showMessageDialog(null, 
                "Error starting application:\n" + e.getMessage() + 
                "\n\nPlease ensure:\n" +
                "1. SQLite JDBC driver (sqlite-jdbc-*.jar) is in lib/ directory\n" +
                "2. You have write permissions in the current directory\n" +
                "3. Java has access to create database files",
                "Startup Error", 
                JOptionPane.ERROR_MESSAGE);
            
            System.exit(1);
        }
    }
} 