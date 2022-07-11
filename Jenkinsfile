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
        stage('Run tests') {
            steps {
                sh 'sudo docker run aksionauivan/selenium-docker'
            }
        }
    }
    post {
        success {
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dockerHub', usernameVariable: 'user', passwordVariable: 'pass']]) {
                sh "sudo docker login --username=${user} --password=${pass}"
                sh "sudo docker push aksionauivan/selenium-docker"
            }
        }
    }
}