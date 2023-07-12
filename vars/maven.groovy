env.App_TYPE = "maven"

def call() {
    pipeline {
        agent any

    environment {
        SONAR= credentials('SONAR')
    }

        stages {
            //for each commit
            stage('Lint Checks') {
                steps {
                    script {
                        lintChecks()
                    }
                }
            }
            stage('sonar Checks') {
                steps {
                    script {
                        sh 'mvn clean compile'
                        env.ARGS="-Dsonar.java.binaries=target/"
                       common.sonarCheck()
                    }
                }
            }
            stage('Test Cases') {

                parallel {

                    stage('Unit Tests') {
                        steps {
                            sh 'echo unit Tests'
                        }
                    }
                    stage('Integration Tests') {
                        steps {
                            sh 'echo Integration Tests'
                        }
                    }
                    stage('Functional Tests') {
                        steps {
                            sh 'echo Functional Tests'
                        }
                    }
                }
            }
        } //end of stages

    }
}
