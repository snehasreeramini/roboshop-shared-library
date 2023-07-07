def lintChecks() {
    sh '''
       # ~/node_modules/jslint/bin/jslint.js server.js
      mvn checkstyle:check
      echo lint check for ${COMPONENT}
'''
}

def sonarCheck() {
    sh '''
       sonar-scanner -Dsonar.host.url=http://172.31.86.224:9000 -Dsonar.source=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}
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
                        sonarCheck()
                    }
                }
            }
        } //end of stages
    }
}
