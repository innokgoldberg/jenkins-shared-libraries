def call(project, domain) {
    addr = "${project}-${env.BUILD_NUMBER}-${env.BRANCH_NAME}.${domain}"
    sh "apt update && apt install ca-certificates libgnutls30"
    sh "go get -d -v -t"
    sh """ADDRESS=${addr} \
        go test ./... -v \
        --run FunctionalTest"""
}
