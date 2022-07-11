pipeline {
    agent any
    stages {
        stage('Build Jar') {
//             agent {
//                 docker {
//                     image 'maven:3-alpine'
//                     args '-v $HOME/.m2:/root/.m2'
//                 }
//             }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Image') {
            steps {
                sh 'sudo docker build -t=aksionauivan/jenkins_with_docker .'
            }
        }
        stage('Push Image') {
            steps {
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId:'dockerHub', usernameVariable: 'user', passwordVariable: 'pass']]) {
                    sh "sudo docker login --username=${user} --password=${pass}"
                    sh "sudo docker push aksionauivan/selenium-docker"
                }
            }
        }
    }
}