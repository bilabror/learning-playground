package com.quizku.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.quizku.database.DatabaseManager;
import com.quizku.models.*;
import com.quizku.services.ScoreCalculator;
import com.quizku.interfaces.Scorable;
import com.quizku.exceptions.DatabaseException;

/**
 * Main frame untuk student
 * Menggunakan ComboBox dan RadioButton sesuai requirement
 */
public class StudentFrame extends JFrame {
    private User currentUser;
    private DatabaseManager dbManager;
    private Scorable scoreCalculator;

    // GUI Components
    private JComboBox<Subject> subjectComboBox;
    private ButtonGroup questionTypeGroup;
    private JRadioButton multipleChoiceRadio;
    private JRadioButton trueFalseRadio;
    private JButton startQuizButton;
    private JButton logoutButton;
    private JTextArea welcomeTextArea;

    public StudentFrame(User user) {
        this.currentUser = user;
        this.dbManager = DatabaseManager.getInstance();
        this.scoreCalculator = new ScoreCalculator();

        initializeComponents();
        setupLayout();
        setupEventListeners();
        setupFrame();
    }

    /**
     * Inisialisasi komponen GUI
     */
    private void initializeComponents() {
        // ComboBox untuk mata pelajaran
        subjectComboBox = new JComboBox<>(Subject.values());
        subjectComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        // RadioButton untuk jenis kuis
        multipleChoiceRadio = new JRadioButton("Pilihan Ganda", true);
        trueFalseRadio = new JRadioButton("Benar/Salah");

        multipleChoiceRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        trueFalseRadio.setFont(new Font("Arial", Font.PLAIN, 14));

        // Group radio buttons
        questionTypeGroup = new ButtonGroup();
        questionTypeGroup.add(multipleChoiceRadio);
        questionTypeGroup.add(trueFalseRadio);

        // Buttons
        startQuizButton = new JButton("Mulai Kuis");
        logoutButton = new JButton("Logout");

        startQuizButton.setFont(new Font("Arial", Font.BOLD, 16));
        startQuizButton.setBackground(new Color(70, 130, 180));
        startQuizButton.setForeground(Color.BLACK);
        startQuizButton.setFocusPainted(false);

        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);

        // Welcome text area
        welcomeTextArea = new JTextArea();
        welcomeTextArea.setEditable(false);
        welcomeTextArea.setBackground(getBackground());
        welcomeTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        welcomeTextArea.setText(getWelcomeText());
    }

    /**
     * Setup layout menggunakan BorderLayout dan panels
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("QUIZKU - Student Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Welcome, " + currentUser.getUsername() + "!");
        userLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        userLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Welcome text
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JScrollPane welcomeScrollPane = new JScrollPane(welcomeTextArea);
        welcomeScrollPane.setPreferredSize(new Dimension(500, 100));
        welcomeScrollPane.setBorder(BorderFactory.createTitledBorder("Selamat Datang"));
        mainPanel.add(welcomeScrollPane, gbc);

        // Subject selection
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel subjectLabel = new JLabel("Pilih Mata Pelajaran:");
        subjectLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(subjectLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(subjectComboBox, gbc);

        // Question type selection
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel typeLabel = new JLabel("Pilih Jenis Kuis:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.add(multipleChoiceRadio);
        radioPanel.add(trueFalseRadio);
        mainPanel.add(radioPanel, gbc);

        // Start quiz button
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);
        mainPanel.add(startQuizButton, gbc);

        // Bottom Panel untuk logout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(logoutButton);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        startQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startQuiz();
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
        setTitle("Quizku - Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    /**
     * Start quiz berdasarkan pilihan user
     */
    private void startQuiz() {
        try {
            Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
            QuestionType selectedType = multipleChoiceRadio.isSelected() ? QuestionType.MULTIPLE_CHOICE
                    : QuestionType.TRUE_FALSE;

            // Ambil soal dari database
            List<Question> questions = dbManager.getQuestions(selectedSubject, selectedType);

            if (questions.isEmpty()) {
                showErrorMessage("Tidak ada soal untuk " + selectedSubject.getDisplayName() +
                        " dengan jenis " + selectedType.getDisplayName());
                return;
            }

            // Buka quiz frame
            QuizFrame quizFrame = new QuizFrame(currentUser, questions, selectedSubject, selectedType, scoreCalculator);
            quizFrame.setVisible(true);
            dispose();

        } catch (DatabaseException ex) {
            showErrorMessage("Error mengambil soal: " + ex.getMessage());
        } catch (Exception ex) {
            showErrorMessage("Terjadi kesalahan: " + ex.getMessage());
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
     * Get welcome text
     */
    private String getWelcomeText() {
        return "Selamat datang di Quizku!\n\n" +
                "Aplikasi kuis edukatif yang akan membantu Anda belajar dengan cara yang menyenangkan.\n" +
                "Pilih mata pelajaran dan jenis kuis yang ingin Anda kerjakan, lalu klik 'Mulai Kuis'.\n\n" +
                "Tips:\n" +
                "• Bacalah setiap soal dengan teliti\n" +
                "• Pikirkan jawaban sebelum memilih\n" +
                "• Jangan terburu-buru dalam menjawab\n" +
                "• Selamat belajar!";
    }

    /**
     * Show error message
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}