#!/usr/bin/env bash
set -euo pipefail
sudo apt update -y
sudo apt install -y openjdk-17-jdk maven git nginx mysql-server
sudo mysql -e "CREATE DATABASE IF NOT EXISTS studentdb;"
sudo mysql -e "CREATE USER IF NOT EXISTS 'smsuser'@'localhost' IDENTIFIED BY 'ChangeMe!';"
sudo mysql -e "GRANT ALL ON studentdb.* TO 'smsuser'@'localhost'; FLUSH PRIVILEGES;"
sudo cp deploy/nginx.conf /etc/nginx/sites-available/sms
sudo ln -sf /etc/nginx/sites-available/sms /etc/nginx/sites-enabled/sms
sudo rm -f /etc/nginx/sites-enabled/default
sudo nginx -t && sudo systemctl reload nginx
echo "Now build the JAR with: mvn clean package -DskipTests"
echo "Then copy target/student-management.jar to /home/ubuntu/student-management/"
echo "Install systemd unit: sudo cp deploy/student-management.service /etc/systemd/system/ && sudo systemctl enable --now student-management"
