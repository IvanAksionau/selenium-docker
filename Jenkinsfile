pipeline {
    agent any
    stages {
        stage('Build Jar') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Image') {
            steps {
                sh 'sudo docker build -t=aksionauivan/selenium-docker .'
            }
        }
        stage('Start Tests infra') {
            steps {
                sh 'sudo docker-compose up --no-color -d hub chrome firefox'
            }
        }
        stage('Run Tests') {
            steps {
                sh 'sudo docker-compose up --no-color test-chrome-module test-firefox-module'
            }
        }
        stage('Stop Tests infra') {
            steps {
                sh 'sudo docker-compose down --no-color'
            }
        }
    }
    post {
        success {
            archiveArtifacts artifacts: 'output/**'
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerHub', usernameVariable: 'user', passwordVariable: 'pass']]) {
                sh "sudo docker login --username=${user} --password=${pass}"
                sh "sudo docker push aksionauivan/selenium-docker"
            }
        }
    }
}