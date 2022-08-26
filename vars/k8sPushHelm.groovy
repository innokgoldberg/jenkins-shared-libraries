def call(project, chartVersion, museumAddr,museumIp,configuration, secrets, replaceTag = false, failIfExists = false) {
    withVault([configuration: configuration, vaultSecrets: secrets]) {
        if (failIfExists) {
            yaml = readYaml file: "helm/${project}/Chart.yaml"
            out = sh returnStdout: true, script: "curl -u ${env.CHARTMUSEUM_USER}:${env.CHARTMUSEUM_PASSWORD} http://${museumAddr}/api/charts/${project}/${yaml.version}"
            if (!out.contains("error")) {
                error "Did you forget to increment the Chart version?"
            }
        }
        if (replaceTag) {
            yaml = readYaml file: "helm/${project}/values.yaml"
            yaml.image.tag = currentBuild.displayName
            sh "rm -f helm/${project}/values.yaml"
            writeYaml file: "helm/${project}/values.yaml", data: yaml
        }
        sh "helm package helm/${project}"
        packageName = "${project}-${chartVersion}.tgz"
        if (chartVersion == "") {
            packageName = sh(returnStdout: true, script: "ls ${project}*").trim()
        }
        sh """curl -u ${env.CHARTMUSEUM_USER}:${env.CHARTMUSEUM_PASSWORD} \
          --data-binary "@${packageName}" \
           -H \"Host: ${museumAddr}\" http://${museumIp}/api/charts"""
    }
}
