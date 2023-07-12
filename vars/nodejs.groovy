env.APP_TYPE = "nodejs"
def call() {
    node {
        common.lintCheck()
        env.ARGS = "-Dsonar.sources=."
        common.sonarCheck()
        common.testCheck()

        if (env.TAG_NAME != null) {
            common.artifacts()
        }
    }


}