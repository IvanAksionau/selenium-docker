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
                sh 'docker run -e HUB_HOST=192.168.100.5 -e MODULE=search-module.xml aksionauivan/selenium-docker'
            }
        }
        stage('Run tests') {
            steps {
                sh 'docker run aksionauivan/selenium-docker'
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