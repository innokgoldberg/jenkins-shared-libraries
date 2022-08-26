 def call(image,configuration, secrets, sudo = true) {
    tagBeta = "${currentBuild.displayName}-${env.BRANCH_NAME}"
    prefix = ""
    if (sudo) {
        prefix = "sudo "
    }
    sh """${prefix}docker pull \
        ${image}:${tagBeta}"""
    sh """${prefix}docker image tag \
        ${image}:${tagBeta} \
        ${image}:${currentBuild.displayName}"""
    sh """${prefix}docker image tag \
        ${image}:${tagBeta} \
        ${image}:latest"""
    withVault([configuration: configuration, vaultSecrets: secrets]) {
            sh """${prefix}docker login \
        -u ${env.DOCKERHUB_USER} -p ${env.DOCKERHUB_PASSWORD}"""
    }
    sh """${prefix}docker image push \
        ${image}:${currentBuild.displayName}"""
    sh """${prefix}docker image push \
        ${image}:latest"""
 }