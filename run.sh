#!/bin/bash
# ──────────────────────────────────────────────────────────────────────────────
#  run.sh  –  Build & Run: Student Management System (Java 21)
# ──────────────────────────────────────────────────────────────────────────────

set -e   # Exit immediately if any command fails

echo ""
echo "  [1/2] Compiling Java source files..."
mkdir -p out

javac -d out \
      -sourcepath src \
      src/model/Student.java \
      src/util/ValidationUtil.java \
      src/service/StudentService.java \
      src/Main.java

echo "        Compilation successful!"
echo ""
echo "  [2/2] Starting application..."
echo ""

java -cp out Main
