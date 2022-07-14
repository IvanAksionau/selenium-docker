pipeline {
    agent any

//    environment {
//        // FOO will be available in entire pipeline
//        FOO = "PIPELINE"
//    }
    stages {
        stage('Build Jar') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Image') {
            steps {
                sh 'docker build -t=aksionauivan/selenium-docker .'
            }
        }
        stage('Start Tests infra') {
            steps {
                sh 'docker-compose up --no-color -d hub chrome firefox'
            }
        }
        stage('Run Tests') {
            steps {
//                sh 'sudo docker-compose up --no-color test-chrome-module test-firefox-module'
                sh 'docker-compose up --no-color test-chrome-module'
                archiveArtifacts artifacts: '**'
            }
        }
        stage('Stop Tests infra') {
            steps {
                sh 'docker-compose down'
            }
        }
    }
    post {
        success {
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerHub', usernameVariable: 'user', passwordVariable: 'pass']]) {
                sh "docker login --username=${user} --password=${pass}"
                sh "docker push aksionauivan/selenium-docker"
            }
        }
    }
}