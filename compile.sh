#!/bin/bash

# Paths
JAVA_FX_LIB="/Users/HP/Downloads/KnittingPlanner/javafx-sdk-21.0.7/lib"
GSON_JAR="src/lib/gson-2.10.1.jar"
SRC_DIR="src"
BIN_DIR="bin"

# Create bin folder if not exists
mkdir -p "$BIN_DIR"

# Compile all Java files with JavaFX and GSON classpaths
javac \
  --module-path "$JAVA_FX_LIB" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "$GSON_JAR" \
  -d "$BIN_DIR" \
  $(find "$SRC_DIR" -name "*.java")

if [ $? -eq 0 ]; then
  echo "✅ Compilation successful."
else
  echo "❌ Compilation failed."
fi

