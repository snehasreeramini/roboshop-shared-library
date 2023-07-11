def lintChecks() {
    sh '''
       # ~/node_modules/jslint/bin/jslint.js server.js
       echo Lint Check for ${COMPONENT}
'''
}

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
                        env.ARGS="-Dsonar.sources=."
                        common.sonarCheck()
                    }
                }
            }
        } //end of stages

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
    }
}