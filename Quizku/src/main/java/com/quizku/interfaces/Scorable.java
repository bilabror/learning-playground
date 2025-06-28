package com.quizku.interfaces;

/**
 * Interface untuk sistem penilaian kuis
 * Menggunakan konsep interface dalam OOP
 */
public interface Scorable {

    /**
     * Menghitung skor berdasarkan jawaban yang benar
     * 
     * @param correctAnswers jumlah jawaban benar
     * @param totalQuestions total jumlah soal
     * @return skor dalam bentuk double
     */
    double calculateScore(int correctAnswers, int totalQuestions);

    /**
     * Menentukan grade berdasarkan skor
     * 
     * @param score skor yang diperoleh
     * @return grade dalam bentuk string
     */
    String getGrade(double score);

    /**
     * Menentukan apakah lulus atau tidak
     * 
     * @param score skor yang diperoleh
     * @return true jika lulus, false jika tidak
     */
    boolean isPassed(double score);
}