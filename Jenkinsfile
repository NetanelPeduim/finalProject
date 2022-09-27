pipeline {
    agent any
    triggers {
        pollSCM '* * * * *'
    }
    options {
      timeout(time: 1, unit: 'MINUTES')
    }
    environment {
        DOCKERHUB_CREDS = credentials('dockerhub_token')
    }
    stages {
        stage('Checkout') {
            steps {
                checkout changelog: true, poll: true, scm: [$class: 'GitSCM', branches: [[name: 'main/*']], extensions: [], userRemoteConfigs: [[credentialsId: 'HIT', url: 'https://github.com/NetanelPeduim/finalProject.git']]]
            }
        }
        stage('Build Container') {
            steps {
                sh "docker build -t netanelped/hit-web-server ./webServer"
                sh "docker tag netanelped/hit-web-server netanelped/hit-web-server:${env.BUILD_ID}"
                /*script {
                    dockerID = docker.build("hit-web-server:${env.BUILD_ID}", "./webServer")
                }*/
            }
        }
        stage('Upload Container') {
            steps {
                    sh "echo $DOCKERHUB_CREDS_PSW | docker login -u $DOCKERHUB_CREDS_USR --password-stdin"
                    sh "docker push netanelped/hit-web-server:${env.BUILD_ID}"
                    sh "docker logout"
            }
        }
    }
    post {
        success {
            print "success"
        }
    }
}
