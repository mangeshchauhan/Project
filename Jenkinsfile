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

        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
                archiveArtifacts artifacts: 'target/student-management.jar', fingerprint: true
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

        stage('Security scanning') {
            steps {
                sh '''
                    mvn -B org.owasp:dependency-check-maven:check \\
                      -DfailBuildOnCVSS=7
                '''
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
                    usernameVariable: 'EC2_USER'
                ), string(credentialsId: 'ec2-host', variable: 'EC2_HOST'),
                    file(credentialsId: 'ec2-known-hosts', variable: 'KNOWN_HOSTS')]) {
                    sh '''
                        SSH_OPTIONS="-o StrictHostKeyChecking=yes -o UserKnownHostsFile=$KNOWN_HOSTS -o IdentitiesOnly=yes"

                        scp $SSH_OPTIONS -i "$SSH_KEY" \\
                          target/student-management.jar "$EC2_USER@$EC2_HOST:/tmp/student-management.jar"

                        ssh $SSH_OPTIONS -i "$SSH_KEY" "$EC2_USER@$EC2_HOST" \\
                          "sudo install -o ubuntu -g ubuntu -m 0644 /tmp/student-management.jar '$DEPLOY_PATH' && \\
                           sudo systemctl daemon-reload && \\
                           sudo systemctl restart student-management && \\
                           sudo systemctl is-active --quiet student-management && \\
                           rm -f /tmp/student-management.jar"
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