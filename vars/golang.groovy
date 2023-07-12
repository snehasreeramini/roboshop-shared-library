 def call() {
    node {
        sh 'rm -rf*'
        git branch: 'main', url: "https://github.com/snehasreeramini/${COMPONENT}"
        env.App_type = "golang"
        common.lintCheck()
        env.ARGS = "-Dsonar.java.binaries=."
        common.sonarCheck()
        common.testCheck()

        if (env.TAG_NAME != null) {
            common.artifacts()
        }
    }


}