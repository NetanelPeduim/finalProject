pipeline {
    agent any
    dockerfile true
    triggers {
        pollSCM '* * * * *'
    }
    options {
      timeout(time: 1, unit: 'MINUTES')
    }
    stages {
        stage('Checkout') {
            steps {
                checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: 'main/*']], extensions: [], userRemoteConfigs: [[credentialsId: 'HIT', url: 'https://github.com/NetanelPeduim/finalProject.git']]]
            }
        }
        stage('Build Container') {
                    steps {
                        script {
                            dockerID = docker.build("hit-web-server:${env.BUILD_ID}")
                            echo $dockerID
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
