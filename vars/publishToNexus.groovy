def call(Map args = [:]) {
  def version = args.version ?: env.VERSION
  def nexusUrl = args.nexusUrl ?: env.NEXUS_URL
  def repo = args.repo ?: 'artifacts'
  def name = args.name ?: 'job-application-tracker'

  if (!version || !nexusUrl) {
    error 'publishToNexus: version et nexusUrl sont obligatoires'
  }

  withEnv(["PUB_VERSION=${version}", "PUB_URL=${nexusUrl}", "PUB_REPO=${repo}", "PUB_NAME=${name}"]) {
    sh '''
      JAR=$(ls target/*.jar | grep -v '\\.original$' | head -n1)
      cp "$JAR" "target/${PUB_NAME}-${PUB_VERSION}.jar"
      curl -fsS -u "$NEXUS_CREDS_USR:$NEXUS_CREDS_PSW" \
        --upload-file "target/${PUB_NAME}-${PUB_VERSION}.jar" \
        "${PUB_URL}/repository/${PUB_REPO}/${PUB_NAME}/${PUB_VERSION}/${PUB_NAME}-${PUB_VERSION}.jar"
    '''
  }
}