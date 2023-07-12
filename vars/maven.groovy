def call() {
    node {
        sh 'rm -rf*'
        git branch: 'main', url: "https://github.com/snehasreeramini/${COMPONENT}"
        env.App_TYPE = "maven"
        common.lintCheckS()
        sh'mvn clean compile'
        env.ARGS = "-Dsonar.java.binaries=target/"
        common.sonarCheck()
        common.testCheck()

        if (env.TAG_NAME != null) {
            common.artifacts()
        }
    }


}
