#!/bin/bash

# Quizku Build and Run Script
echo "Building Quizku Application..."

# Create build directory
mkdir -p build/classes

# Compile Java files with SQLite JDBC driver in classpath
echo "Compiling Java files..."
javac -cp "lib/sqlite-jdbc-3.34.0.jar" \
      -d build/classes \
      src/main/java/com/quizku/**/*.java \
      src/main/java/com/quizku/*.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Running Quizku Application..."
    
    # Run the application
    java -cp "build/classes:lib/sqlite-jdbc-3.34.0.jar" com.quizku.Main
else
    echo "Compilation failed. Please check for errors."
    exit 1
fi 