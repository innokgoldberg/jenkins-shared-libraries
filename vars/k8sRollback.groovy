def call(project) {
    sh """helm rollback \
        ${project} 0 """
    error "Failed production tests"
}