# ==============================================================================
#  Dockerfile – Student Management System (Java 21)
#  Multi-stage build:
#    Stage 1 (builder) – compiles the Java source files
#    Stage 2 (runtime) – copies only the .class files into a lean JRE image
#
#  This keeps the final image small: the JDK (compiler) is NOT shipped
#  in the production image – only the slim JRE is.
# ==============================================================================


# ──────────────────────────────────────────────────────────────────────────────
#  STAGE 1 : builder
#  Base image : eclipse-temurin:21-jdk-alpine  (JDK needed to compile)
# ──────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set a clean working directory inside the container
WORKDIR /app

# Copy the entire src/ folder into the container
COPY src/ ./src/

# Create the output directory and compile all source files
RUN mkdir -p out && \
    javac -d out \
          -sourcepath src \
          src/model/Student.java \
          src/util/ValidationUtil.java \
          src/service/StudentService.java \
          src/Main.java


# ──────────────────────────────────────────────────────────────────────────────
#  STAGE 2 : runtime
#  Base image : eclipse-temurin:21-jre-alpine  (JRE only – no compiler)
#  This image is ~100 MB smaller than the full JDK image.
# ──────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

# Metadata labels (good practice for image registries)
LABEL maintainer="mangesh.225@gmail.com"
LABEL description="Student Management System – Core Java 21 Console App"
LABEL version="1.0.0"

# Working directory in the runtime container
WORKDIR /app

# Copy ONLY the compiled .class files from the builder stage
COPY --from=builder /app/out ./out/

# Default command: run the application
# -it flag will be needed at docker run time for interactive console input
CMD ["java", "-cp", "out", "Main"]
