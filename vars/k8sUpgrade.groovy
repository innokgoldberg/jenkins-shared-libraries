def call(project, addr) {
    sh """helm upgrade \
        ${project} \
        helm/${project} -i \
        --namespace ${project} \
        --set image.tag=${currentBuild.displayName} \
        --set ingress.host=${addr} \
        --reuse-values"""
}