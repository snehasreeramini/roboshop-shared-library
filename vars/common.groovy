def sonarCheck() {

    sh '''
       sonar-scanner -Dsonar.host.url=http://172.31.86.224:9000 -Dsonar.source=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} ${ARGS}
       sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} 172.31.86.224 ${COMPONENT}
'''
}
