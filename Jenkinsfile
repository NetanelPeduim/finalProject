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
                sh "docker build -t netanelped/${env.JOB_NAME} ./webServer"
                sh "docker tag netanelped/${env.JOB_NAME} netanelped/${env.JOB_NAME}:${env.BUILD_ID}"
            }
        }
        stage('Upload Container') {
            steps {
                    sh "echo $DOCKERHUB_CREDS_PSW | docker login -u $DOCKERHUB_CREDS_USR --password-stdin"
                    sh "docker push netanelped/${env.JOB_NAME}:${env.BUILD_ID}"
                    sh "docker logout"
            }
        }
        stage('Deploying to QA') {
            steps {
                sh "docker run -d -p 90:80 --name application-qa netanelped/${env.JOB_NAME}"
            }
        }
        stage('Running QA Auto Tests') {
            steps {
                script {
                    def response
                    try {
                        response = httpRequest validResponseCodes: '200', url: 'http://localhost:90'
                    }
                    catch(Exception e) {
                        currentBuild.result = 'FAILED'
                        error("Aborting the build.")
                    }
                    def status = response.status
                    println "status is " + status
                    if(status != 200) {
                        print "FAILED TEST"
                        exit()
                    }
                }
            }
        }
        stage('Running Selenium Tests') {
            steps {
                sh './automation/gradlew test'
            }
        }
    }
    post {
        success {
            print "success"
        }
        always {
            script {
                sh 'docker stop $(docker ps -aq)'
                sh 'docker rm $(docker ps -aq)'
            }
        }
    }
}
