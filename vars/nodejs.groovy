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
            SONAR = credentials('SONAR')
            NEXUS = credentails('NEXUS')
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
                        env.ARGS = "-Dsonar.sources=."
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
            stage('Prepare Artifacts') {
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh '''
                     npm install
                     zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
'''
                }
            }


            stage('Upload Artifacts') {
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh '''
                       curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://34.230.80.134:8081/repository/${COMPONENT}/${COMPONENT}.zip-${TAG_NAME}

'''
                }
            }

        } //end of stages
    }
}
