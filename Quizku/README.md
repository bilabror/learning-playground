# Quizku - Aplikasi Kuis Edukatif

**Quizku** adalah aplikasi kuis edukatif berbasis Java Desktop yang dirancang untuk membantu siswa dalam belajar dan menguji pemahaman mereka terhadap berbagai mata pelajaran melalui kuis interaktif. Aplikasi ini menyediakan dua jenis pengguna, yaitu:

- **Admin** yang bertugas mengelola soal.
- **Siswa** yang dapat mengerjakan kuis.

## 🎯 Fitur Utama

- **Sistem Login**: Autentikasi pengguna berdasarkan peran (Admin/Siswa).
- **Manajemen Soal (Admin)**: Admin dapat menambah, melihat, mengubah, dan menghapus (CRUD) soal.
- **Pengerjaan Kuis (Siswa)**: Siswa dapat memilih mata pelajaran dan jenis kuis (Pilihan Ganda / Benar-Salah).
- **Kuis Interaktif**: Antarmuka pengerjaan kuis yang dilengkapi dengan navigasi dan timer.
- **Penilaian Otomatis**: Hasil kuis ditampilkan secara langsung, lengkap dengan skor, grade, dan status kelulusan.
- **Integrasi Database**: Menggunakan SQLite untuk menyimpan data pengguna, soal, dan hasil kuis.

## 🛠️ Tumpukan Teknologi

- **Bahasa**: Java 11+
- **GUI Framework**: Java Swing
- **Database**: SQLite
- **Pola Desain**: Singleton, Factory
- **Arsitektur**: Layered architecture (Model, Service, GUI, Database)

## 📁 Struktur Proyek

```
.
├── lib/
│   └── sqlite-jdbc-3.34.0.jar
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── quizku/
│                   ├── Main.java
│                   ├── database/
│                   ├── exceptions/
│                   ├── gui/
│                   ├── interfaces/
│                   ├── models/
│                   └── services/
└── quizku.db
```

## 🚀 Pengaturan dan Instalasi

### Prasyarat

- Java Development Kit (JDK) 11 atau yang lebih baru.
- Akses ke terminal atau command prompt.

### Langkah-langkah Instalasi

1.  **Clone repositori ini** (atau unduh dan ekstrak file ZIP).
2.  Buka terminal dan navigasikan ke direktori proyek.
3.  **Jalankan build script**:
    - Untuk pengguna Linux/macOS:
      ```bash
      chmod +x build.sh
      ./build.sh
      ```
    - Untuk pengguna Windows, Anda dapat menjalankan perintah kompilasi dan eksekusi secara manual.

## 🎮 Cara Menggunakan

### Akun Default

- **Admin**:
  - Username: `admin`
  - Password: `admin123`
- **Siswa**:
  - Username: `student`
  - Password: `student123`

### Alur Kerja Admin

1.  Login dengan akun Admin.
2.  `AdminFrame` akan ditampilkan.
3.  Admin dapat memilih untuk menambahkan soal baru, yang akan membuka `AddQuestionDialog`.
4.  Admin mengisi detail soal dan menyimpannya ke database.

### Alur Kerja Siswa

1.  Login dengan akun Siswa atau daftar jika belum punya akun.
2.  `StudentFrame` akan ditampilkan.
3.  Siswa memilih mata pelajaran dan jenis kuis.
4.  `QuizFrame` akan muncul, dan siswa dapat mulai mengerjakan kuis.
5.  Setelah selesai, hasil kuis (skor, nilai, status kelulusan) akan ditampilkan.

## 📊 Skema Database

Aplikasi ini menggunakan SQLite dengan tiga tabel utama:

- `users`: Menyimpan data pengguna (admin dan siswa).
- `questions`: Menyimpan soal-soal kuis.
- `quiz_results`: Menyimpan hasil kuis yang dikerjakan oleh siswa.

## 🏗️ Konsep OOP yang Diimplementasikan

- **Enkapsulasi**: Field privat dengan metode getter/setter.
- **Pewarisan (Inheritance)**: `MultipleChoiceQuestion` dan `TrueFalseQuestion` merupakan turunan dari kelas abstrak `Question`.
- **Polimorfisme**: Penggunaan metode abstrak di kelas `Question`.
- **Exception Handling**: Menggunakan exception kustom seperti `DatabaseException`.

## 📝 Lisensi

Proyek ini dibuat untuk tujuan pendidikan.
