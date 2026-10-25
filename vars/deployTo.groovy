def call(String environment, Map args = [:]) {
  def version = args.version ?: env.VERSION
  def nexusUrl = args.nexusUrl ?: env.NEXUS_URL

  if (!environment || !version || !nexusUrl) {
    error 'deployTo: environment, version et nexusUrl sont obligatoires'
  }

  withEnv(["DEPLOY_ENV=${environment}", "DEPLOY_VERSION=${version}", "DEPLOY_NEXUS_URL=${nexusUrl}"]) {
    sh '''
      NEXUS_URL="$DEPLOY_NEXUS_URL" NEXUS_USER="$NEXUS_CREDS_USR" NEXUS_PASSWORD="$NEXUS_CREDS_PSW" \
        sh ci/scripts/deploy.sh "$DEPLOY_ENV" "$DEPLOY_VERSION"
    '''
  }
}