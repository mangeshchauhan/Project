# Student Management System — Docker Guide

## Project Structure

```
StudentManagement/
├── src/
│   ├── model/Student.java
│   ├── service/StudentService.java
│   ├── util/ValidationUtil.java
│   └── Main.java
├── Dockerfile
├── .dockerignore
├── docker-compose.yml
└── run.sh
```

---

## How the Docker Setup Works

### Multi-Stage Build

```
┌─────────────────────────────┐     ┌──────────────────────────────┐
│  STAGE 1 : builder          │     │  STAGE 2 : runtime           │
│  eclipse-temurin:21-jdk     │────▶│  eclipse-temurin:21-jre      │
│                             │     │                              │
│  • Copies src/              │     │  • Copies only out/*.class   │
│  • Runs javac               │     │  • Runs: java -cp out Main   │
│  • Produces out/*.class     │     │  • No compiler included      │
└─────────────────────────────┘     └──────────────────────────────┘
     ~350 MB (not shipped)                  ~180 MB (final image)
```

- **Stage 1 (builder):** Uses the full JDK to compile source code
- **Stage 2 (runtime):** Uses only the JRE — smaller and more secure

---

## Quick Start

### Option A — Docker CLI

```bash
# 1. Build the image
docker build -t student-management:1.0.0 .

# 2. Run the container interactively
docker run -it --rm student-management:1.0.0
```

### Option B — Docker Compose

```bash
# Build and run in one command
docker compose run --rm app

# Or: build first, then run separately
docker compose build
docker compose run --rm app
```

---

## All Docker Commands Reference

```bash
# ── Build ──────────────────────────────────────────────────────────────────

# Build image with a tag
docker build -t student-management:1.0.0 .

# Build with no cache (force fresh build)
docker build --no-cache -t student-management:1.0.0 .

# Build and show verbose output
docker build --progress=plain -t student-management:1.0.0 .


# ── Run ────────────────────────────────────────────────────────────────────

# Run interactively (required – app uses Scanner for input)
docker run -it --rm student-management:1.0.0

# Run with a custom container name
docker run -it --rm --name sms-app student-management:1.0.0

# Run in detached mode (NOT recommended – app needs stdin)
# docker run -d student-management:1.0.0   ← will exit immediately


# ── Image Management ───────────────────────────────────────────────────────

# List all images
docker images

# Inspect the image (size, layers, config)
docker inspect student-management:1.0.0

# See image layer history and sizes
docker history student-management:1.0.0

# Remove the image
docker rmi student-management:1.0.0


# ── Container Management ───────────────────────────────────────────────────

# List running containers
docker ps

# List all containers (including stopped)
docker ps -a

# Stop a running container
docker stop sms-app

# Remove a stopped container
docker rm sms-app

# Enter a running container's shell (for debugging)
docker exec -it sms-app sh


# ── Cleanup ────────────────────────────────────────────────────────────────

# Remove all stopped containers
docker container prune

# Remove all unused images
docker image prune

# Remove everything (containers, images, volumes, cache)
docker system prune -a
```

---

## Push to Docker Hub

```bash
# 1. Log in to Docker Hub
docker login

# 2. Tag the image with your Docker Hub username
docker tag student-management:1.0.0 your-dockerhub-username/student-management:1.0.0

# 3. Push the image
docker push your-dockerhub-username/student-management:1.0.0

# 4. Anyone can now pull and run it
docker run -it --rm your-dockerhub-username/student-management:1.0.0
```

---

## Sample Build Output

```
[+] Building 18.3s
 => [builder 1/4] FROM eclipse-temurin:21-jdk-alpine
 => [builder 2/4] WORKDIR /app
 => [builder 3/4] COPY src/ ./src/
 => [builder 4/4] RUN mkdir -p out && javac ...
 => [runtime 1/3] FROM eclipse-temurin:21-jre-alpine
 => [runtime 2/3] WORKDIR /app
 => [runtime 3/3] COPY --from=builder /app/out ./out/
 => exporting to image
 => naming to docker.io/library/student-management:1.0.0

Image size: ~185 MB
```

---

## Why -it Flag Is Required

This app uses `Scanner(System.in)` to read keyboard input.  
Without `-it`, the container has no terminal attached and exits immediately.

| Flag | Meaning                              |
|------|--------------------------------------|
| `-i` | Keep STDIN open (send keyboard input)|
| `-t` | Allocate a pseudo-TTY (show prompts) |
| `--rm` | Auto-delete container after exit   |
