pipeline {
    agent any

    environment {
        DEPLOY_PATH = '/home/ubuntu/student-management/student-management.jar'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Java and Maven') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Build Application') {
            steps {
                sh 'mvn -B clean package -DskipTests'
                archiveArtifacts artifacts: 'target/student-management.jar', fingerprint: true
            }
        }

        stage('Deploy to EC2') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([sshUserPrivateKey(
                    credentialsId: 'ec2-ssh-key',
                    keyFileVariable: 'SSH_KEY',
                    usernameVariable: 'EC2_USER',
                    passphraseVariable: 'SSH_PASSPHRASE'
                ), string(credentialsId: 'ec2-host', variable: 'EC2_HOST')]) {
                    sh '''
                        scp -o StrictHostKeyChecking=no -i "$SSH_KEY" \\
                          target/student-management.jar "$EC2_USER@$EC2_HOST:/tmp/student-management.jar"
                        ssh -o StrictHostKeyChecking=no -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" \\
                          "sudo install -o ubuntu -g ubuntu -m 0644 /tmp/student-management.jar '$DEPLOY_PATH' && \\
                           sudo systemctl daemon-reload && \\
                           sudo systemctl restart student-management && \\
                           sudo systemctl is-active --quiet student-management"
                        ssh -o StrictHostKeyChecking=no -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" \\
                          'rm -f /tmp/student-management.jar'
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Build and EC2 deployment completed successfully.'
        }
    }
}