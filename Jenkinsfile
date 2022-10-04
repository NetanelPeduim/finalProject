pipeline {
    agent any
    triggers {
        pollSCM '* * * * *'
    }
    options {
      timeout(time: 10, unit: 'MINUTES')
    }
    environment {
        DOCKERHUB_CREDS = credentials('dockerhub_token')
        def deployToProd = false
        def qaTestsPassed = false
    }
    stages {
        stage('Checkout') {
            steps {
                echo 'checking out code..'
                checkout changelog: true, poll: true, scm: [$class: 'GitSCM', branches: [[name: 'main']], extensions: [], userRemoteConfigs: [[credentialsId: 'HIT', url: 'https://github.com/NetanelPeduim/finalProject.git']]]
                script {result = sh (script: "git log -1 | grep 'v*'", returnStatus: true)
                if (result != 0) {
                    deployToProd = true
                }}
                echo 'checkout done!'
            }
        }
        stage('Build Container') {
            steps {
                echo 'Building container..'
                sh "docker build -t netanelped/${env.JOB_NAME} ./webServer"
                sh "docker tag netanelped/${env.JOB_NAME} netanelped/${env.JOB_NAME}:${env.BUILD_ID}"
                echo 'Build complete!'
            }
        }
        stage('Upload Container') {
            steps {
                    echo 'Uploading container to dockerhub..'
                    sh "echo $DOCKERHUB_CREDS_PSW | docker login -u $DOCKERHUB_CREDS_USR --password-stdin"
                    sh "docker push netanelped/${env.JOB_NAME}:${env.BUILD_ID}"
                    sh "docker logout"
                    echo 'Upload done!'
            }
        }
        stage('Deploying to QA') {
            steps {
                echo 'Deploying container application-qa netanelped/${env.JOB_NAME}..'
                sh "docker run -d -p 90:80 --name application-qa netanelped/${env.JOB_NAME}"
                echo 'Deploy done!'
            }
        }
        stage('Running QA Auto Tests') {
            steps {
                echo 'Running tests to make sure container is up..'
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
                echo 'tests complete!'
            }
        }
        stage('Running Selenium Tests') {
            steps {
                echo 'Running selenium automation tests..'
                sh 'gradle -p ./automation clean'
                sh 'gradle -p ./automation test'
                echo 'tests complete!'
            }
        }
        stage('Test Results') {
            steps {
                junit './automation/build/test-results/test/*.xml'
            }
        }
        stage('Deploy To Prod') {
            steps { script {
            echo 'shutting down application-qa container\n'
            sh 'docker stop $(docker ps -aq)'
            echo 'removing application-qa container\n'
            sh 'docker rm $(docker ps -aq)'
            if(deployToProd && qaTestsPassed) {
                echo 'need to deploy to prod!\n'
                echo 'revving up application to prod!'
                sh "docker run -d -p 80:80 --name application-prod netanelped/${env.JOB_NAME}"
                echo 'deployed to prod!'
            }
            else {
                    echo 'skipping deployment to prod!\n'
                }
            }}
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
                echo 'complete!'
            }
        }
    }
}
