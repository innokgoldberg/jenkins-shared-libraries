def call(image, configuration, secrets, sudo = true) {
    tagBeta = "${currentBuild.displayName}-${env.BRANCH_NAME}"
    prefix = ""
    if (sudo) {
        prefix = "sudo "
    }

    sh """${prefix}docker image build \
            -t ${image}:${tagBeta} ."""
    withVault([configuration: configuration, vaultSecrets: secrets]) {
            sh """${prefix}docker login \
        -u ${env.DOCKERHUB_USER} -p ${env.DOCKERHUB_PASSWORD}"""
    }
    sh """${prefix}docker image push \
            ${image}:${tagBeta}"""
}