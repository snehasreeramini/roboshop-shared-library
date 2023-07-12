env.APP_TYPE = "nodejs"
def call() {
    node {
        git branch: 'main', url: "https://github.com/snehasreeramini/${COMPONENT}"
        common.lintCheck()
        env.ARGS = "-Dsonar.sources=."
        common.sonarCheck()
        common.testCheck()

        if (env.TAG_NAME != null) {
            common.artifacts()
        }
    }


}