pipeline {
    agent any
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
                // Get some code from a GitHub repository
                //git 'https://github.com/jglick/simple-maven-project-with-tests.git'

                // Run Maven on a Unix agent.
                //sh "mvn -Dmaven.test.failure.ignore=true clean package"

                // To run Maven on a Windows agent, use
                // bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }
        stage('Build Container') {
                    steps {
                        script {
                            dockerID = docker.build 'https://github.com/NetanelPeduim/finalProject.git#main:/webServer/.'
                            echo $dockerID
                        }

                    }
                }

    }
    post {
                                    // If Maven was able to run the tests, even if some of the test
                                    // failed, record the test results and archive the jar file.
                                    success {
                                        print "success"
                                        //junit '**/target/surefire-reports/TEST-*.xml'
                                        //archiveArtifacts 'target/*.jar'
                                    }
                                }
}
