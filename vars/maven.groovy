def lintChecks() {
    sh '''
       # ~/node_modules/jslint/bin/jslint.js server.js
      mvn checkstyle:check
      echo lint check for ${COMPONENT}
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
                        sh 'mvn clean compile'
                        env.ARGS="-Dsonar.java.binaries=target/"
                       common.sonarCheck()
                    }
                }
            }
        } //end of stages
    }
}
