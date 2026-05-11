# Student Management System

Production-ready Spring Boot 3 + Java 17 + MySQL 8 + Thymeleaf + Bootstrap 5 app with REST APIs, Spring Security, Docker, and AWS EC2 deployment.

## Features
- Login / Logout (Spring Security, BCrypt, session-based, CSRF)
- Dashboard with totals + recent students
- CRUD students with validation, search, pagination
- REST API at `/api/students`
- SLF4J + Logback logging to `logs/`
- Global exception handling + custom error pages
- Docker multi-stage build + docker-compose with MySQL
- GitHub Actions CI/CD pipeline
- systemd unit + Nginx reverse proxy config

Default login: **admin / admin123**

## Project Structure
```
src/main/java/com/example/sms
  ├── controller/       (web + api)
  ├── service/
  ├── repository/
  ├── entity/
  ├── dto/
  ├── config/ security/
  └── exception/
src/main/resources
  ├── templates/  static/  db/  application.properties
deploy/   .github/workflows/   Dockerfile  docker-compose.yml
```

## Run Locally

### With Docker (easiest)
```bash
docker compose up --build
# open http://localhost:8080
```

### Without Docker
```bash
# 1) MySQL
mysql -uroot -p < src/main/resources/db/schema.sql
mysql -uroot -p < src/main/resources/db/data.sql

# 2) Build + run
mvn clean package -DskipTests
java -jar target/student-management.jar
```

## REST API
| Method | Path | Description |
|---|---|---|
| GET | `/api/students?page=0&size=10&q=` | List/search/paginate |
| GET | `/api/students/{id}` | Get one |
| POST | `/api/students` | Create |
| PUT | `/api/students/{id}` | Update |
| DELETE | `/api/students/{id}` | Delete |

All `/api/**` requires HTTP Basic / session auth.

---

## AWS EC2 Ubuntu Deployment

### 1. Launch EC2
- Ubuntu 22.04, t2.micro+, **Security Group**: open ports **22, 80, 8080**
- Attach an **Elastic IP** so the public IP doesn't change

### 2. Install dependencies
```bash
sudo apt update -y
sudo apt install -y openjdk-17-jdk maven git nginx mysql-server
```

### 3. Clone + build
```bash
git clone <your-repo-url> ~/student-management-src
cd ~/student-management-src
mvn clean package -DskipTests
mkdir -p ~/student-management
cp target/student-management.jar ~/student-management/
cp deploy/.env.example ~/student-management/.env
# edit ~/student-management/.env with real DB credentials
```

### 4. MySQL
```bash
sudo mysql <<SQL
CREATE DATABASE studentdb;
CREATE USER 'smsuser'@'localhost' IDENTIFIED BY 'ChangeMe!';
GRANT ALL ON studentdb.* TO 'smsuser'@'localhost';
FLUSH PRIVILEGES;
SQL
```

### 5. systemd service
```bash
sudo cp deploy/student-management.service /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable --now student-management
sudo systemctl status student-management
```

### 6. Nginx reverse proxy (port 80 -> 8080)
```bash
sudo cp deploy/nginx.conf /etc/nginx/sites-available/sms
sudo ln -sf /etc/nginx/sites-available/sms /etc/nginx/sites-enabled/sms
sudo rm -f /etc/nginx/sites-enabled/default
sudo nginx -t && sudo systemctl reload nginx
```

### 7. Access
Open **http://<EC2-PUBLIC-IP>** — login with `admin / admin123` and **change the password immediately**.

### 8. Hardening
- Restrict SSH (port 22) to your IP in the Security Group
- Close port 8080 once Nginx is fronting it
- Add HTTPS with Let's Encrypt: `sudo apt install certbot python3-certbot-nginx && sudo certbot --nginx`
- Rotate the seeded admin password (update the `users` table)
- Use AWS Secrets Manager or SSM Parameter Store for DB credentials

## CI/CD
Configure these GitHub secrets: `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`, `EC2_HOST`, `EC2_USER`, `EC2_SSH_KEY`, `DB_HOST`, `DB_USER`, `DB_PASSWORD`. Pushes to `main` build, dockerize, push, and SSH-deploy.
