pipeline {
    agent none
    stages {
        stage('Build Jar') {
//             agent {
//                 docker {
//                     image 'maven:3-alpine'
//                     args '-v $HOME/.m2:/root/.m2'
//                 }
//             }
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }
        stage('Build Image') {
            steps {
                script {
                    app = sudo.docker.build("aksionauivan/selenium-docker")
                }
            }
        }
//         stage('Push Image') {
//             steps {
//                 script {
// //                 'dockerHub' is credentials for https://hub.docker.com/, created in Jenkins manually (manage global credentials)
// 			        sudo.docker.withRegistry('https://registry.hub.docker.com', 'dockerHub') {
// // 			        	app.push("${BUILD_NUMBER}") can be tagged based on Jenkins build number
// 			            app.push("latest")
// 			        }
//                 }
//             }
//         }
        stage('Push Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'pass', usernameVariable: 'user')])
                bat "sudo docker login --username=${user} --password=${pass}"
                bat "sudo docker push aksionauivan/selenium-docker:latest"
            }
        }
    }
}