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
    }
}