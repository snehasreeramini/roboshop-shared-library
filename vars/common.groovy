def sonarCheck() {
    stage('Sonal Code Analysis') {
        sh '''
       sonar-scanner -Dsonar.host.url=http://172.31.86.224:9000 -Dsonar.source=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} ${ARGS}
       sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.86.224 ${COMPONENT}
'''
    }
}

def lintChecks() {
    stage('Lint Checks')
    if (env.App_type == "nodejs") {
        sh '''
       # ~/node_modules/jslint/bin/jslint.js server.js
       echo Lint Check for ${COMPONENT}
'''
    }

    else if (env.App_type == "golang"){
        sh '''
       # ~/node_modules/jslint/bin/jslint.js server.js
       echo Lint Check for ${COMPONENT}
'''
    }
    else if (env.App_type == "python"){
        sh '''
       # ~/node_modules/jslint/bin/jslint.js server.js
      pylint *.py
      echo lint check for ${COMPONENT}   
'''
    }
    else if (env.App_type == "maven"){
        sh '''
       # ~/node_modules/jslint/bin/jslint.js server.js
      mvn checkstyle:check
      echo lint check for ${COMPONENT}
'''
    }
}


def TestCases() {
    stage('Test Cases') {
        def stages = [:]

         stages['Unit Tests'] {
                    sh 'echo unit Tests'
                }
            stages['Integration Tests'] {
                sh 'echo Integration Tests'
            }
            stages['Functional Tests'] {
                    sh 'echo Functional Tests'
                }
             parallel (stages)
            }
        }

 def artifacts(){
       stage('check the Release'){
         def UPLOAD_STATUS = sh(returnStdout: true, script: "curl -L -s http://34.230.80.134:8081/services/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip")
         print UPLOAD_STATUS
}
     if (env.UPLOAD_STATUS == "") {

         stage('Prepare Artifacts') {
           if(env.APP_TYPE == "nodejs") {
               sh '''
                     npm install
                     zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
'''
           }
             if(env.APP_TYPE == "maven") {
                 sh '''
                     npm install
                     zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
'''
             }
             if(env.APP_TYPE == "python") {
                 sh '''
                     npm install
                     zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
'''
             }
         }
        stage('Upload Artifacts') {
            witgCredentials([usernamePassword(credentialsId: 'NUXUS', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]) {
                sh '''
                       curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://34.230.80.134:8081/repository/${COMPONENT}/${COMPONENT}.zip-${TAG_NAME}

'''
            }
        }
 }
 }

