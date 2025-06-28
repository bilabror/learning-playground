package com.quizku.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.quizku.models.*;
import com.quizku.exceptions.DatabaseException;

/**
 * Database Manager untuk aplikasi Quizku menggunakan SQLite
 * Menggunakan Singleton pattern
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DB_URL = "jdbc:sqlite:quizku.db";
    private Connection connection;
    
    // Private constructor untuk Singleton
    private DatabaseManager() {
        try {
            // Explicitly load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Create connection
            connection = DriverManager.getConnection(DB_URL);
            
            // Enable foreign key constraints for SQLite
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            
            System.out.println("Database connection established successfully!");
            
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("SQLite JDBC driver not found. Please ensure sqlite-jdbc jar is in classpath.", e);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database: " + e.getMessage(), e);
        }
    }
    
    // Singleton getInstance method
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Inisialisasi database dengan membuat tabel-tabel yang diperlukan
     */
    public void initializeDatabase() throws DatabaseException {
        try {
            createTables();
            insertDefaultData();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to initialize database", e);
        }
    }
    
    /**
     * Membuat tabel-tabel yang diperlukan
     */
    private void createTables() throws SQLException {
        String[] createTableQueries = {
            // Tabel users
            """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                role TEXT NOT NULL CHECK (role IN ('ADMIN', 'STUDENT'))
            )
            """,
            
            // Tabel questions
            """
            CREATE TABLE IF NOT EXISTS questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                question_text TEXT NOT NULL,
                subject TEXT NOT NULL,
                question_type TEXT NOT NULL CHECK (question_type IN ('MULTIPLE_CHOICE', 'TRUE_FALSE')),
                points INTEGER NOT NULL DEFAULT 10,
                option_a TEXT,
                option_b TEXT,
                option_c TEXT,
                option_d TEXT,
                correct_answer TEXT NOT NULL
            )
            """,
            
            // Tabel quiz_results
            """
            CREATE TABLE IF NOT EXISTS quiz_results (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                subject TEXT NOT NULL,
                question_type TEXT NOT NULL,
                correct_answers INTEGER NOT NULL,
                total_questions INTEGER NOT NULL,
                score REAL NOT NULL,
                grade TEXT NOT NULL,
                passed BOOLEAN NOT NULL,
                completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users (id)
            )
            """
        };
        
        for (String query : createTableQueries) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(query);
            }
        }
    }
    
    /**
     * Insert data default untuk testing
     */
    private void insertDefaultData() throws SQLException {
        // Cek apakah sudah ada admin default
        if (!userExists("admin")) {
            insertUser(new User("admin", "admin123", "admin@quizku.com", UserRole.ADMIN));
        }
        
        // Cek apakah sudah ada student default
        if (!userExists("student")) {
            insertUser(new User("student", "student123", "student@quizku.com", UserRole.STUDENT));
        }
        
        // Insert beberapa contoh soal jika belum ada
        if (getQuestionCount() == 0) {
            insertSampleQuestions();
        }
    }
    
    /**
     * Insert contoh soal untuk testing
     */
    private void insertSampleQuestions() throws SQLException {
        // Soal Matematika - Multiple Choice
        MultipleChoiceQuestion mathQ1 = new MultipleChoiceQuestion(
            "Berapakah hasil dari 15 + 23?",
            Subject.MATEMATIKA, 10,
            "35", "38", "40", "42", "B"
        );
        insertQuestion(mathQ1);
        
        MultipleChoiceQuestion mathQ2 = new MultipleChoiceQuestion(
            "Berapakah hasil dari 8 × 7?",
            Subject.MATEMATIKA, 10,
            "54", "56", "58", "60", "B"
        );
        insertQuestion(mathQ2);
        
        // Soal Matematika - True/False
        TrueFalseQuestion mathTF1 = new TrueFalseQuestion(
            "Hasil dari 10 ÷ 2 adalah 5",
            Subject.MATEMATIKA, 10, true
        );
        insertQuestion(mathTF1);
        
        // Soal Bahasa Indonesia - Multiple Choice
        MultipleChoiceQuestion indoQ1 = new MultipleChoiceQuestion(
            "Kata yang tepat untuk melengkapi kalimat 'Dia ... ke sekolah setiap hari' adalah:",
            Subject.BAHASA_INDONESIA, 10,
            "pergi", "berangkat", "datang", "pulang", "A"
        );
        insertQuestion(indoQ1);
        
        // Soal Bahasa Indonesia - True/False
        TrueFalseQuestion indoTF1 = new TrueFalseQuestion(
            "Kata 'cantik' termasuk kata sifat",
            Subject.BAHASA_INDONESIA, 10, true
        );
        insertQuestion(indoTF1);
    }
    
    // ==================== USER OPERATIONS ====================
    
    /**
     * Insert user baru ke database
     */
    public boolean insertUser(User user) throws DatabaseException {
        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getRole().name());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert user", e);
        }
    }
    
    /**
     * Authenticasi user login
     */
    public User authenticateUser(String username, String password) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    UserRole.valueOf(rs.getString("role"))
                );
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to authenticate user", e);
        }
    }
    
    /**
     * Cek apakah username sudah ada
     */
    public boolean userExists(String username) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check user existence", e);
        }
    }
    
    // ==================== QUESTION OPERATIONS ====================
    
    /**
     * Insert soal ke database
     */
    public boolean insertQuestion(Question question) throws DatabaseException {
        String sql = """
            INSERT INTO questions (question_text, subject, question_type, points, 
                                 option_a, option_b, option_c, option_d, correct_answer) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, question.getQuestionText());
            pstmt.setString(2, question.getSubject().name());
            pstmt.setString(3, question.getType().name());
            pstmt.setInt(4, question.getPoints());
            
            if (question instanceof MultipleChoiceQuestion mc) {
                pstmt.setString(5, mc.getOptionA());
                pstmt.setString(6, mc.getOptionB());
                pstmt.setString(7, mc.getOptionC());
                pstmt.setString(8, mc.getOptionD());
            } else {
                pstmt.setString(5, null);
                pstmt.setString(6, null);
                pstmt.setString(7, null);
                pstmt.setString(8, null);
            }
            
            pstmt.setString(9, question.getCorrectAnswer());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert question", e);
        }
    }
    
    /**
     * Ambil soal berdasarkan subject dan type
     */
    public List<Question> getQuestions(Subject subject, QuestionType type) throws DatabaseException {
        String sql = "SELECT * FROM questions WHERE subject = ? AND question_type = ?";
        List<Question> questions = new ArrayList<>();
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, subject.name());
            pstmt.setString(2, type.name());
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Question question = createQuestionFromResultSet(rs);
                questions.add(question);
            }
            
            return questions;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get questions", e);
        }
    }
    
    /**
     * Buat object Question dari ResultSet
     */
    private Question createQuestionFromResultSet(ResultSet rs) throws SQLException {
        QuestionType type = QuestionType.valueOf(rs.getString("question_type"));
        Subject subject = Subject.valueOf(rs.getString("subject"));
        
        if (type == QuestionType.MULTIPLE_CHOICE) {
            return new MultipleChoiceQuestion(
                rs.getInt("id"),
                rs.getString("question_text"),
                subject,
                rs.getInt("points"),
                rs.getString("option_a"),
                rs.getString("option_b"),
                rs.getString("option_c"),
                rs.getString("option_d"),
                rs.getString("correct_answer")
            );
        } else {
            return new TrueFalseQuestion(
                rs.getInt("id"),
                rs.getString("question_text"),
                subject,
                rs.getInt("points"),
                Boolean.parseBoolean(rs.getString("correct_answer"))
            );
        }
    }
    
    /**
     * Hitung jumlah soal dalam database
     */
    public int getQuestionCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM questions";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count questions", e);
        }
    }
    
    // ==================== QUIZ RESULT OPERATIONS ====================
    
    /**
     * Simpan hasil kuis
     */
    public boolean saveQuizResult(QuizResult result) throws DatabaseException {
        String sql = """
            INSERT INTO quiz_results (user_id, subject, question_type, correct_answers, 
                                    total_questions, score, grade, passed) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, result.getUserId());
            pstmt.setString(2, ""); // Subject akan diambil dari context
            pstmt.setString(3, ""); // Question type akan diambil dari context
            pstmt.setInt(4, result.getCorrectAnswers());
            pstmt.setInt(5, result.getTotalQuestions());
            pstmt.setDouble(6, result.getScore());
            pstmt.setString(7, result.getGrade());
            pstmt.setBoolean(8, result.isPassed());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save quiz result", e);
        }
    }
    
    /**
     * Tutup koneksi database
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
} 