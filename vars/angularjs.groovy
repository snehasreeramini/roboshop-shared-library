def call() {
    node {
        sh 'rm -rf*'
        git branch: 'main', url: "https://github.com/snehasreeramini/${COMPONENT}"
        env.APP_TYPE = "nginx"
        common.lintCheck()
        env.ARGS = "-Dsonar.sources=."
        common.sonarCheck()
        common.testCheck()

        if (env.TAG_NAME != null) {
            common.artifacts()
        }
    }


}