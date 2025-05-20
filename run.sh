#!/bin/bash

# Paths
JAVA_FX_LIB="/Users/HP/Downloads/KnittingPlanner/javafx-sdk-21.0.7/lib"
GSON_JAR="src/lib/gson-2.10.1.jar"
BIN_DIR="bin"

# Run the compiled application
java \
  --module-path "$JAVA_FX_LIB" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "$BIN_DIR:$GSON_JAR" \
  knitplanner.Main

