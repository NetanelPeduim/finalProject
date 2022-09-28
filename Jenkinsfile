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
        def deployToProd = false
        def qaTestsPassed = false
    }
    stages {
        stage('Checkout') {
            steps {
                checkout changelog: true, poll: true, scm: [$class: 'GitSCM', branches: [[name: 'main']], extensions: [], userRemoteConfigs: [[credentialsId: 'HIT', url: 'https://github.com/NetanelPeduim/finalProject.git']]]
                result = sh (script: "git log -1 | grep 'v*'", returnStatus: true)
                if (result != 0) {
                    deployToProd = true
                }
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
                        response = httpRequest quiet:true, validResponseCodes: '200', url: 'http://localhost:90'
                    }
                    catch(Exception e) {
                        currentBuild.result = 'FAILED'
                        error("Aborting the build.")
                    }
                    def status = response.status
                    if(status != 200) {
                        println "FAILED TEST"
                        exit()
                    }
                    else {
                        println "WebServer is up!"
                        qaTestsPassed = true
                    }
                }
            }
        }
        stage('Running Selenium Tests') {
            steps {
                sh 'gradle -p ./automation clean'
                sh 'gradle -p ./automation test'
            }
        }
        stage('Test Results') {
            steps {
                junit './automation/build/test-results/test/*.xml'
            }
        }
        stage('Deploy To Prod') {
            steps {
            echo 'shutting down application-qa container\n'
            sh 'docker stop $(docker ps -aq)'
            echo 'removing application-qa container\n'
            sh 'docker rm $(docker ps -aq)'
            if(deployToProd && qaTestsPassed) {
                echo 'need to deploy to prod!\n'
                echo 'revving up application to prod!'
                sh "docker run -d -p 80:80 --name application-prod netanelped/${env.JOB_NAME}"
            }
            else {
                    echo 'skipping deployment to prod!\n'
                }
            }
        }
    }
    post {
        success {
            print "success"
        }
        always {
            script {
                echo 'stopping and removing ALL dockers..'
                sh 'docker stop $(docker ps -aq)'
                sh 'docker rm $(docker ps -aq)'
            }
        }
    }
}
