def lintChecks() {
    sh '''
       # ~/node_modules/jslint/bin/jslint.js server.js
      pylint *.py
      echo lint check for ${COMPONENT}
      
'''
}

def call() {
    pipeline {
        agent any

        stages {
            //for each commit
            stage('Lint Checks') {
                steps {
                    script {
                        lintChecks()
                    }
                }
            }
        } //end of stages
    }
}
