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
            }
        }
        stage('Upload Container') {
            steps {
                    sh "echo $DOCKERHUB_CREDS_PSW | docker login -u $DOCKERHUB_CREDS_USR --password-stdin"
                    sh "docker push netanelped/hit-web-server:${env.BUILD_ID}"
                    sh "docker logout"
            }
        }
        /*stage('Deploying to QA') {
            steps {
                sh "docker run -d -p 80:80 --name application-qa netanelped/hit-web-server"
            }
        }*/
        stage('Running QA Auto Tests') {
            steps {
                script {
                    try {
                        def response = httpRequest validResponseCodes: '200', url: 'http://localhost'
                    }
                    catch(Exception e) {
                        currentBuild.result = 'FAILED'
                        currentBuild.description = 'Invalid HTTP response'
                        error("Aborting the build.")
                    }
                }

            }
        }
    }
    post {
        success {
            print "success"
        }
    }
}
