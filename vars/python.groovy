env.App_TYPE = "python"
def call() {
    node {
        sh 'rm -rf*'  //cleaning the content
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