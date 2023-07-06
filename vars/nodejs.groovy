def lintChecks() {
    sh '''
       # ~/node_modules/jslint/bin/jslint.js server.js
       echo Lint Check for ${COMPONENT}
'''
}

def call(COMPONENT) {
    pipeline {
        agent any

        stages {
            //for each commit
            stage('Lint Checks') {
                steps {
                    script {
                        lintChecks(COMPONENT)
                    }
                }
            }
        } //end of stages
    }
}
}